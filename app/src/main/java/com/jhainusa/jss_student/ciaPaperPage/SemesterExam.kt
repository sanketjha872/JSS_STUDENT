package com.jhainusa.jss_student.ciaPaperPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jhainusa.jss_student.AllScreenNav
import com.jhainusa.jss_student.LottieLoader
import com.jhainusa.jss_student.R
import com.jhainusa.jss_student.classComp
import kotlinx.coroutines.delay

@Composable
fun SemesterListScreen(
    yearId: String,
    viewModel: PapersViewModel = viewModel(),
    onSemesterSelected: (String) -> Unit
) {
    val semesters by viewModel.semester.collectAsState()

    LaunchedEffect(yearId) {
        viewModel.loadsemesters(yearId)
        com.jhainusa.jss_student.AnalyticsHelper.logScreenView("SemesterListScreen", "SemesterExam")
        com.jhainusa.jss_student.AnalyticsHelper.logEvent("view_semesters", android.os.Bundle().apply {
            putString("year_id", yearId)
        })
    }
    when{
        semesters == null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.DarkGray, strokeCap = StrokeCap.Round)
            }
        }
        semesters!!.isEmpty() -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                LottieLoader("",R.raw.coming_soon)
            }
        }
        else -> {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                items(semesters!!) { semId ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0xFFE9DCE5))
                            .clickable{
                                onSemesterSelected(semId)
                            }
                            .padding(
                                horizontal = 18.dp,
                                vertical = 20.dp
                            )

                    ) {
                        Text(
                            text = semId,
                            fontFamily = FontFamily(
                                Font(R.font.plusjakartasansmedium)
                            ),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                        Box(
                            modifier = Modifier.padding(5.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .padding(8.dp)
                        ){
                            Icon(
                                painter = painterResource(R.drawable.file_pdf_svgrepo_com),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)

                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}
