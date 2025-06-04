package com.jhainusa.jss_student

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.airbnb.lottie.compose.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhainusa.jss_student.GeminiBackend.encodeImageToBase64
import com.jhainusa.jss_student.GeminiBackend.sendToGemini
import com.jhainusa.jss_student.RoomDatabase.MainVIewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun DropdownMenuExample(vIewModel: MainVIewModel) {
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
                        apiKey = "AIzaSyCjLHvAfyn_TmcV9oVxAePdJOesrkFtcLQ",
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
                    expanded = !expanded
                }
            ) {
        Icon(
            painter = painterResource(R.drawable.upload_square_svgrepo_com),
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(33.dp)
        )
    }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false},
            modifier = Modifier.background(Color.Transparent,RoundedCornerShape(18.dp))
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
            ) {
                DropdownMenuItem(onClick = {
                    expanded = false
                    launcher.launch("image/*")
                }) {
                    Text(
                        text = "Upload",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontFamily = plusJak,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        painter = painterResource(R.drawable.ai_svgrepo_com),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(17.dp)
                    )
                }
                DropdownMenuItem(onClick = {
                    expanded = false
                }) {
                    Text(
                        text = "Edit",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontFamily = plusJak,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        painter = painterResource(R.drawable.edit_svgrepo_com),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
        }
    if (loading) {
        LottieLoader()
    }
}
@Preview
@Composable
fun LottieLoader(message: String = "Analyzing your image...") {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.handloader))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp)
            ) {
                LottieAnimation(
                    composition,
                    progress,
                    modifier = Modifier.size(250.dp)
                )
                Text(
                    text = message,
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontFamily = plusJak
                )
            }
    }
}
