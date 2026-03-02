package com.jhainusa.jss_student

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhainusa.jss_student.GeminiBackend.encodeImageToBase64
import com.jhainusa.jss_student.GeminiBackend.sendToGemini
import com.jhainusa.jss_student.RoomDatabase.MainVIewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun DropdownMenuExample(
    vIewModel: MainVIewModel,
    isEditMode: Boolean,
    onEditModeToggle: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var loading by remember{mutableStateOf(false)}
    var expanded by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            loading = true
            scope.launch {
                try {
                    val base64 = withContext(Dispatchers.IO) {
                        val stream = context.contentResolver.openInputStream(it)
                        encodeImageToBase64(stream!!)
                    }
                    sendToGemini(
                        apiKey = "AIzaSyC4fOZLV3qaIWmNjfM5HotQXYFN6g4qzAE",
                        base64,
                        vIewModel
                    ) { response ->
                        loading = false
                        Log.d("Gemini", "Response: $response")
                    }
                } catch (e: Exception) {
                    loading = false
                    e.printStackTrace()
                }
            }
        }
    }
    IconButton(
                onClick = {
                    if (isEditMode) {
                        onEditModeToggle()
                    } else {
                        expanded = !expanded
                    }
                }
            ) {
        Icon(
            painter = painterResource(if (isEditMode) R.drawable.baseline_check_24 else R.drawable.upload_square_svgrepo_com),
            contentDescription = if (isEditMode) "Done" else "More Options",
            tint = if (isEditMode) Color(0xFF2E7D32) else Color(0xFF6B7280),
            modifier = Modifier.size(29.dp)
        )
    }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false}
        ) {
                DropdownMenuItem(onClick = {
                    expanded = false
                    launcher.launch("image/*")
                }
                ) {
                    Text(
                        text = "Upload",
                        color = Color.Black,
                        fontSize = 15.sp,
                        fontFamily = plusJak,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(30.dp))
                    Icon(
                        painter = painterResource(R.drawable.ai_svgrepo_com),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
                DropdownMenuItem(onClick = {
                    expanded = false
                    onEditModeToggle()
                }) {
                    Text(
                        text = "Edit",
                        color = Color.Black,
                        fontSize = 15.sp,
                        fontFamily = plusJak,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        painter = painterResource(R.drawable.edit_svgrepo_com),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )

            }
        }
    if (loading) {
        LottieLoader("Analyzing your image...",R.raw.handloader)
    }
}
