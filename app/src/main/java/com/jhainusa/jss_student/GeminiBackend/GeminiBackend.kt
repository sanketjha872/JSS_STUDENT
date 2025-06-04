package com.jhainusa.jss_student.GeminiBackend

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.jhainusa.jss_student.RoomDatabase.MainVIewModel
import com.jhainusa.jss_student.RoomDatabase.Schedule
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


fun sendToGemini(apiKey: String, base64Image: String,vIewModel: MainVIewModel, onResult: (Boolean) -> Unit) {
    val request = GeminiRequest(
        contents = listOf(
            Content(
                parts = listOf(
                    Part(
                        text = "Extract this timetable into JSON format with fields like day, subject,time and teacher." +
                                "and don't give me subject code like BAS 403 etc only give subject name like MATHS and teacher which is like AD and day like Mon case"
                    ),
                    Part(inline_data = InlineData("image/jpeg", base64Image))
                )
            )
        )
    )

    GeminiClient.instance.generateContent(apiKey, request).enqueue(object : Callback<GeminiResponse> {
        override fun onResponse(call: Call<GeminiResponse>, response: Response<GeminiResponse>) {
            if (response.isSuccessful && response.body() != null) {
                val jsonString =
                    response.body()!!.candidates[0].content.parts[0].text.replace("```json", "")
                        .replace("```", "")
                        .trim()
                val jsonArray = JSONArray(jsonString)
                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)
                    val day = item.getString("day")
                    val time = item.getString("time")
                    val subject = item.getString("subject")
                    val teacher = item.getString("teacher")
                    vIewModel.insertSchedule(
                        Schedule(
                            day =day,
                            subject = subject,
                            time = time,
                            teacher = teacher
                        )
                    )

                    Log.d("Timetable", "Day: $day, Subject: $subject ,time : $time , teacher : $teacher")
                }
                onResult(true)
            }
        }

        override fun onFailure(call: Call<GeminiResponse>, t: Throwable) {
            onResult(false)
        }
    })
}