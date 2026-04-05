package com.jhainusa.jss_student.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.lerp
import com.jhainusa.jss_student.R
import com.jhainusa.jss_student.plusJak
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

data class OnboardingPage(
    val title: String,
    val description: String,
    val buttonText: String,
    val imageRes: Int
)

val onboardingPages = listOf(
    OnboardingPage(
        title = "AI Timetable\nOrganizer",
        description = "Upload your timetable image and\nlet AI organize it instantly.",
        buttonText = "Continue",
        imageRes = R.drawable.img1 // Placeholder
    ),
    OnboardingPage(
        title = "Swipe to\nMark Attendance",
        description = "Mark attendance with a simple swipe\nright for present, left for absent.",
        buttonText = "Continue",
        imageRes = R.drawable.mark_attendance_with_swipe_gestures // Placeholder
    ),
    OnboardingPage(
        title = "Smart\nNotifications",
        description = "Get timely notifications for your upcoming\nclasses and stay on track effortlessly.",
        buttonText = "Get Started",
        imageRes = R.drawable.smartnotif // Placeholder
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit = {},
    onSkip: () -> Unit = {}
) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = Color.White,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 16.dp, end = 24.dp)
            ) {
                TextButton(
                    onClick = onSkip,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        text = "Skip >",
                        fontSize = 16.sp,
                        fontFamily = plusJak,
                        color = Color.Gray
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) { pageIndex ->
                val pageOffset = (
                        (pagerState.currentPage - pageIndex) + pagerState.currentPageOffsetFraction
                        ).absoluteValue

                val scale = lerp(
                    start = 0.85f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                )

                val alpha = lerp(
                    start = 0.5f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha

                        },
                    contentAlignment = Alignment.Center
                ) {
                    OnboardingContent(onboardingPages[pageIndex])
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 60.dp, start = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Page Indicator
                Row(
                    modifier = Modifier.padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(onboardingPages.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        val pageOffset = (
                                (pagerState.currentPage - index) + pagerState.currentPageOffsetFraction
                                ).absoluteValue

                        val scale = lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )

                        val alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                        Box(
                            modifier = Modifier
                                .width(if (isSelected) 24.dp else 8.dp)
                                .height(8.dp)
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                    this.alpha = alpha

                                }
                                .background(
                                    color = if (isSelected) Color(0xFF262626) else Color(0xFFE0E0E0),
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                    }
                }

                Button(
                    onClick = {
                        if (pagerState.currentPage < onboardingPages.size - 1) {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            onFinish()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF2E2E33),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.elevation(0.dp)
                ) {
                    Text(
                        text = onboardingPages[pagerState.currentPage].buttonText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = plusJak
                    )
                }
            }
        }
    }
}

@Composable
fun OnboardingContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image Section
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(1f)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = page.title,
            fontSize = 36.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = plusJak,
            color = Color.Black,
            textAlign = TextAlign.Center,
            lineHeight = 44.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = page.description,
            fontSize = 15.sp,
            fontFamily = plusJak,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen()
}
