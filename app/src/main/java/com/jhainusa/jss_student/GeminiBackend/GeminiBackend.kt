package com.jhainusa.jss_student.GeminiBackend

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import com.jhainusa.jss_student.RoomDatabase.DaySchedule
import com.jhainusa.jss_student.RoomDatabase.MainVIewModel
import com.jhainusa.jss_student.RoomDatabase.Schedule
import com.jhainusa.jss_student.assignColor
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.InputStream


fun encodeImageToBase64(inputStream: InputStream): String {
    val bitmap = BitmapFactory.decodeStream(inputStream)

    // Resize if width > 800
    val resizedBitmap = if (bitmap.width > 800) {
        val aspectRatio = bitmap.height.toDouble() / bitmap.width
        Bitmap.createScaledBitmap(bitmap, 800, (800 * aspectRatio).toInt(), true)
    } else bitmap

    val outputStream = ByteArrayOutputStream()
    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream) // Compress more (40%)

    return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
}


fun sendToGemini(apiKey: String, base64Image: String, viewModel: MainVIewModel, onResult: (Boolean) -> Unit) {
    val request = GeminiRequest(
        contents = listOf(
            Content(
                parts = listOf(
                    Part(
                        text = "Extract this timetable into JSON format as a list of objects. Each object MUST have: 'day', 'subject', 'time', and 'teacher'. " +
                                "Rules: " +
                                "1. 'day' should be short (Mon, Tue, Wed, Thu, Fri, Sat). " +
                                "2. 'subject' should be in short form, NOT the code (e.g., use 'OS' not 'BCS403'). " +
                                "3. 'teacher' should be the name or initials provided. " +
                                "4. 'time' should be in format 'HH:MM AM/PM - HH:MM AM/PM'. " +
                                "5. If it's a LAB, it usually lasts 2 hours and for C1 and C2 lab subject are different. " +
                                "6. Do NOT include exams or lunch breaks or mentoring or remedial classes . " +
                                "Output ONLY the raw JSON array."
                    ),
                    Part(inline_data = InlineData("image/jpeg", base64Image))
                )
            )
        )
    )

    GeminiClient.instance.generateContent(apiKey, request).enqueue(object : Callback<GeminiResponse> {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onResponse(call: Call<GeminiResponse>, response: Response<GeminiResponse>) {
            if (response.isSuccessful && response.body() != null) {
                try {
                    val jsonString = response.body()!!.candidates[0].content.parts[0].text
                        .replace("```json", "")
                        .replace("```", "")
                        .trim()
                    
                    val jsonArray = JSONArray(jsonString)
                    
                    // Group by subject and teacher to combine different days/times
                    val subjectsMap = mutableMapOf<String, MutableList<DaySchedule>>()
                    val teachersMap = mutableMapOf<String, String>()

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val day = item.getString("day")
                        val time = item.getString("time")
                        val subject = item.getString("subject").uppercase().trim()
                        val teacher = item.optString("teacher", "Unknown")

                        if (!subjectsMap.containsKey(subject)) {
                            subjectsMap[subject] = mutableListOf()
                            teachersMap[subject] = teacher
                        }
                        subjectsMap[subject]?.add(DaySchedule(day, time))
                    }

                    // Insert grouped schedules into database
                    subjectsMap.forEach { (subjectName, schedules) ->
                        viewModel.insertSchedule(
                            Schedule(
                                subject = subjectName,
                                teacher = teachersMap[subjectName] ?: "Unknown",
                                scheduleday = schedules,
                                color = assignColor(subjectName).value.toLong()
                            )
                        )
                    }
                    onResult(true)
                } catch (e: Exception) {
                    Log.e("Gemini", "Error parsing JSON: ${e.message}")
                    onResult(false)
                }
            } else {
                onResult(false)
            }
        }

        override fun onFailure(call: Call<GeminiResponse>, t: Throwable) {
            onResult(false)
        }
    })
}
