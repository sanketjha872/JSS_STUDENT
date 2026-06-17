package com.jhainusa.jss_student

import android.os.Build
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jhainusa.jss_student.RoomDatabase.ClassSchedule
import com.jhainusa.jss_student.RoomDatabase.MainVIewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.ceil

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BunkAnalyticsScreen(viewModel: MainVIewModel, subjectId: Int) {
    val subject by viewModel.observeSchedule(subjectId).observeAsState()
    val attendanceHistory by viewModel.getAttendanceHistory(subjectId).observeAsState(emptyList())

    androidx.compose.runtime.LaunchedEffect(subjectId) {
        AnalyticsHelper.logScreenView("BunkAnalyticsScreen", "BunkAnalytics")
        subject?.let {
            AnalyticsHelper.logEvent("view_bunk_analytics", android.os.Bundle().apply {
                putString("subject", it.subject)
            })
        }
    }

    var startDate by remember { mutableStateOf(LocalDate.now().minusMonths(1)) }
    var endDate by remember { mutableStateOf(LocalDate.now()) }
    
    // For date selection dialogs
    var showStartDatePicker by remember { mutableStateOf(false) }

    val filteredAttendance = attendanceHistory.filter {
        val date = LocalDate.parse(it.date, DateTimeFormatter.ISO_DATE)
        !date.isBefore(startDate) && !date.isAfter(endDate)
    }

    val totalClassesInRange = filteredAttendance.size
    val attendedClassesInRange = filteredAttendance.count { it.attendanceStatus == 1 }
    val missedClassesInRange = filteredAttendance.count { it.attendanceStatus == 2 }
    val rangeAttendanceRate = if (totalClassesInRange > 0) (attendedClassesInRange * 100 / totalClassesInRange) else 0

    // Overall stats for bunk prediction
    val totalClassesOverall = attendanceHistory.size
    val attendedClassesOverall = attendanceHistory.count { it.attendanceStatus == 1 }
    val overallAttendanceRate = if (totalClassesOverall > 0) (attendedClassesOverall.toDouble() / totalClassesOverall) else 0.0

    val predictionText: String
    val predictionTitle: String
    val predictionSubtitle: String
    val predictionColor: Color

    if (overallAttendanceRate >= 0.75) {
        val maxBunks = ((attendedClassesOverall / 0.75) - totalClassesOverall).toInt()
        predictionTitle = "$maxBunks More"
        predictionSubtitle = "Safe bunks remaining"
        predictionText = "You can miss $maxBunks more classes to stay above 75%."
        predictionColor = Color(0xFFFBE7D7)
    } else {
        // formula: (attended + x) / (total + x) >= 0.75  => attended + x >= 0.75*total + 0.75*x => 0.25x >= 0.75*total - attended => x >= 3*total - 4*attended
        val classesToAttend = if (totalClassesOverall > 0) {
            ceil(3.0 * totalClassesOverall - 4.0 * attendedClassesOverall).toInt().coerceAtLeast(0)
        } else {
            0
        }
        predictionTitle = "Next $classesToAttend"
        predictionSubtitle = "Classes to hit 75%"
        predictionText = "Attend the next $classesToAttend classes to reach 75% attendance."
        predictionColor = Color(0xFFDBF8EB)
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            ) {
                Text(
                        text = "Analytics",
                        fontFamily = FontFamily(Font(R.font.plusjakartasansbold)),
                        color = Color(0xFF1A1A1A),
                        fontSize = 30.sp,
                    )
                Spacer(modifier = Modifier.height(9.dp))
                Text(
                    text = subject?.subject ?: "Loading...",
                    color = Color(0xFF6B7280),
                    fontFamily = FontFamily(Font(R.font.plusjakartasansregular)),
                    fontSize = 18.sp,
                )
            }
        },
        modifier = Modifier.background(Color.White).statusBarsPadding(),
        containerColor = Color.White,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(14.dp))

            AttendanceCalendarCard(attendanceHistory)

            Spacer(modifier = Modifier.height(24.dp))

            RangeSummarySection(
                rate = "$rangeAttendanceRate%",
                attended = attendedClassesInRange.toString(),
                missed = missedClassesInRange.toString(),
                dateRangeText = "Showing: ${startDate.format(DateTimeFormatter.ofPattern("MMM d"))} - ${endDate.format(DateTimeFormatter.ofPattern("MMM d"))}",
                onEditRange = { showStartDatePicker = true }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Two small cards
            Row(modifier = Modifier.fillMaxWidth()) {
                StatsSmallCard(
                    modifier = Modifier.weight(1f),
                    title = predictionTitle,
                    subtitle = predictionSubtitle,
                    icon = ImageVector.vectorResource(R.drawable.schoolbell),
                    backgroundColor = predictionColor
                )
                Spacer(modifier = Modifier.width(16.dp))
                StatsSmallCard(
                    modifier = Modifier.weight(1f),
                    title = "${attendedClassesOverall}/${totalClassesOverall}",
                    subtitle = "Total Attendance",
                    icon = Icons.Default.ElectricBolt,
                    backgroundColor = Color(0xFFDBEEFB)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            FuturePredictionCard(predictionText)

            Spacer(modifier = Modifier.height(80.dp)) // Padding for bottom nav
        }
    }

    if (showStartDatePicker) {
        DateRangePickerDialog(
            initialStart = startDate,
            initialEnd = endDate,
            onDismiss = { showStartDatePicker = false },
            onRangeSelected = { start, end ->
                startDate = start
                endDate = end
                showStartDatePicker = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateRangePickerDialog(
    initialStart: LocalDate,
    initialEnd: LocalDate,
    onDismiss: () -> Unit,
    onRangeSelected: (LocalDate, LocalDate) -> Unit
) {
    val state = rememberDateRangePickerState()
    
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val start = state.selectedStartDateMillis?.let { 
                    java.time.Instant.ofEpochMilli(it).atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                } ?: initialStart
                val end = state.selectedEndDateMillis?.let { 
                    java.time.Instant.ofEpochMilli(it).atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                } ?: initialEnd
                onRangeSelected(start, end)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DateRangePicker(state = state, modifier = Modifier.weight(1f))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AttendanceCalendarCard(attendanceHistory: List<ClassSchedule>) {
    val accentColor = Color(0xFF262626)
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    
    val attendanceMap = attendanceHistory.associateBy { it.date }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = plusJak,
                    color = Color(0xFF262626)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { currentMonth = currentMonth.minusMonths(1) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.ChevronLeft,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = accentColor
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(
                    onClick = { currentMonth = currentMonth.plusMonths(1) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = accentColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT").forEach { day ->
                Text(
                    text = day,
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    fontFamily = plusJak,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val firstDayOfMonth = currentMonth.atDay(1)
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // 0 for Sunday
        val daysInMonth = currentMonth.lengthOfMonth()
        
        val weeks = mutableListOf<List<String>>()
        var currentWeek = mutableListOf<String>()
        
        for (i in 0 until firstDayOfWeek) {
            currentWeek.add("")
        }
        
        for (i in 1..daysInMonth) {
            currentWeek.add(i.toString())
            if (currentWeek.size == 7) {
                weeks.add(currentWeek)
                currentWeek = mutableListOf()
            }
        }
        
        if (currentWeek.isNotEmpty()) {
            while (currentWeek.size < 7) {
                currentWeek.add("")
            }
            weeks.add(currentWeek)
        }

        weeks.forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                week.forEach { day ->
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (day.isNotEmpty()) {
                            val date = currentMonth.atDay(day.toInt())
                            val dateString = date.format(DateTimeFormatter.ISO_DATE)
                            val attendance = attendanceMap[dateString]
                            
                            val bgColor = when(attendance?.attendanceStatus) {
                                1 -> Color(0xFF77BB7E) // Present - Green
                                2 -> Color(0xF3F24D4D) // Absent - Red
                                else -> Color.Transparent
                            }
                            val textColor = when(attendance?.attendanceStatus) {
                                1 -> Color.White
                                2 -> Color.White
                                else -> Color(0xFF4B5563)
                            }

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(bgColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day,
                                    fontSize = 16.sp,
                                    fontFamily = plusJak,
                                    color = textColor,
                                    fontWeight = if (attendance != null) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RangeSummarySection(rate: String, attended: String, missed: String, dateRangeText: String, onEditRange: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF3F4F6))
                .clickable { onEditRange() }
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = dateRangeText,
                    fontSize = 14.sp,
                    fontFamily = plusJak,
                    color = Color(0xFF4B5563)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Edit",
                    fontSize = 14.sp,
                    fontFamily = plusJak,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "RANGE SUMMARY",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = plusJak,
            color = Color(0xFF4B5563),
            letterSpacing = 1.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            RangeSummaryItem(
                modifier = Modifier.weight(1f),
                label = "RATE",
                value = rate,
                backgroundColor = Color(0xFFE2DFFB),
                textColor = Color(0xFF1A1A1A),
                shape = RoundedCornerShape(20.dp,5.dp,5.dp,20.dp)
            )
            RangeSummaryItem(
                modifier = Modifier.weight(1f),
                label = "ATTENDED",
                value = attended,
                backgroundColor = Color(0xFFDBEEFB),
                textColor = Color(0xFF1A1A1A),
                shape = RoundedCornerShape(5.dp)

            )
            RangeSummaryItem(
                modifier = Modifier.weight(1f),
                label = "MISSED",
                value = missed,
                backgroundColor = Color(0xFFF9E0E0),
                textColor = Color(0xFF1A1A1A),
                shape = RoundedCornerShape(5.dp,20.dp,20.dp,5.dp)
            )
        }
    }
}


@Composable
fun RangeSummaryItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    backgroundColor: Color,
    textColor: Color,
    shape : RoundedCornerShape
) {
    Column(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontFamily = plusJak,
            fontWeight = FontWeight.Bold,
            color = textColor.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = value,
            fontSize = 28.sp,
            fontFamily = plusJak,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun StatsSmallCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    backgroundColor: Color
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .padding(vertical = 20.dp, horizontal = 18.dp)
            .height(120.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.padding(horizontal = 2.dp).size(24.dp),
            tint = PrimaryColor
        )
        Column(){
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = plusJak,
                color = PrimaryColor,
                modifier = Modifier.padding(4.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color.DarkGray,
                fontFamily = plusJak,
                modifier = Modifier.clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(5.dp)

            )
        }
    }
}

@Composable
fun FuturePredictionCard(predictionText: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(PrimaryColor)
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Outlined.Lightbulb,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Future Prediction",
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = plusJak
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = predictionText,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = plusJak,
            color = Color.White,
            lineHeight = 28.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Info,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Threshold: 75%",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontFamily = plusJak
                )
            }
        }
    }
}

