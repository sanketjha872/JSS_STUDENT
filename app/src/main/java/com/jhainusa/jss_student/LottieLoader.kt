package com.jhainusa.jss_student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import androidx.compose.ui.tooling.preview.Preview
import com.jhainusa.jss_student.ui.theme.JSS_STUDENTTheme

@Composable
fun LottieLoader(message: String,resId: Int) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(20.dp)
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

@Preview(showBackground = true)
@Composable
fun LottieLoaderPreview() {
    JSS_STUDENTTheme {
        LottieLoader(
            message = "Loading...",
            resId = R.raw.loader
        )
    }
}

