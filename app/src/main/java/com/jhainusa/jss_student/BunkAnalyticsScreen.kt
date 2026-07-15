package com.jhainusa.jss_student

import android.os.Build
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhainusa.jss_student.RoomDatabase.ClassSchedule
import com.jhainusa.jss_student.RoomDatabase.MainVIewModel
import com.jhainusa.jss_student.RoomDatabase.Schedule
import android.graphics.Bitmap
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
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

    val minDate = remember(attendanceHistory) {
        attendanceHistory.mapNotNull {
            try { LocalDate.parse(it.date) } catch (e: Exception) { null }
        }.minOrNull() ?: LocalDate.now().minusMonths(3)
    }

    var startDate by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    var endDate by remember { mutableStateOf(LocalDate.now()) }
    
    // For date selection dialogs
    var showStartDatePicker by remember { mutableStateOf(false) }

    val filteredAttendance = attendanceHistory.filter {
        val date = try { LocalDate.parse(it.date, DateTimeFormatter.ISO_DATE) } catch(e: Exception) { null }
        date != null && !date.isBefore(startDate) && !date.isAfter(endDate)
    }

    val totalClassesInRange = filteredAttendance.size
    val attendedClassesInRange = filteredAttendance.count { it.attendanceStatus == 1 }
    val missedClassesInRange = filteredAttendance.count { it.attendanceStatus == 2 }
    val rangeAttendanceRate = if (totalClassesInRange > 0) (attendedClassesInRange.toDouble() / totalClassesInRange) else 0.0
    val rangeAttendanceRatePercent = (rangeAttendanceRate * 100).toInt()


    val predictionText: String
    val predictionTitle: String
    val predictionSubtitle: String
    val predictionColor: Color

    if (rangeAttendanceRate >= 0.75) {
        val maxBunks = if (totalClassesInRange > 0) ((attendedClassesInRange / 0.75) - totalClassesInRange).toInt() else 0
        predictionTitle = "$maxBunks More"
        predictionSubtitle = "Safe bunks in range"
        predictionText = "In this period, you could miss $maxBunks more classes to stay above 75%."
        predictionColor = Color(0xFFFBE7D7)
    } else {
        // formula: (attended + x) / (total + x) >= 0.75  => attended + x >= 0.75*total + 0.75*x => 0.25x >= 0.75*total - attended => x >= 3*total - 4*attended
        val classesToAttend = if (totalClassesInRange > 0) {
            ceil(3.0 * totalClassesInRange - 4.0 * attendedClassesInRange).toInt().coerceAtLeast(0)
        } else {
            0
        }
        predictionTitle = "Next $classesToAttend"
        predictionSubtitle = "Needed in range"
        predictionText = "To reach 75% for this period, you would need to attend $classesToAttend more classes."
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
                        fontFamily = FontFamily(Font(R.font.plusjakartasansmedium)),
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1A1A1A),
                        fontSize = 30.sp,
                    )
                Spacer(modifier = Modifier.height(9.dp))
                Text(
                    text = subject?.subject ?: "Loading...",
                    color = Color(0xFF6B7280),
                    fontFamily = plusJak,
                    fontWeight = FontWeight.Normal,
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

            AttendanceCalendarCard(attendanceHistory, startDate, endDate)

            Spacer(modifier = Modifier.height(24.dp))

            DateRangeFilterPresets(
                minDate = minDate,
                onRangeSelected = { start, end ->
                    startDate = start
                    endDate = end
                },
                currentStart = startDate,
                currentEnd = endDate
            )

            RangeSummarySection(
                rate = "$rangeAttendanceRatePercent%",
                attended = attendedClassesInRange.toString(),
                missed = missedClassesInRange.toString(),
                total = totalClassesInRange.toString(),
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
                    title = "${attendedClassesInRange}/${totalClassesInRange}",
                    subtitle = "Range Attendance",
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
    val state = rememberDateRangePickerState(
        initialSelectedStartDateMillis = initialStart.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli(),
        initialSelectedEndDateMillis = initialEnd.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
    )
    
    val pickerColors = DatePickerDefaults.colors(
        containerColor = Color.White,
        titleContentColor = Color(0xFF6B7280),
        headlineContentColor = Color(0xFF262626),
        weekdayContentColor = Color(0xFF9CA3AF),
        subheadContentColor = Color(0xFF262626),
        yearContentColor = Color(0xFF262626),
        currentYearContentColor = Color(0xFF262626),
        selectedYearContainerColor = Color(0xFF262626),
        selectedYearContentColor = Color.White,
        dayContentColor = Color(0xFF1F2937),
        disabledDayContentColor = Color.Gray.copy(alpha = 0.3f),
        selectedDayContainerColor = Color(0xFF262626),
        selectedDayContentColor = Color.White,
        todayContentColor = Color(0xFF262626),
        todayDateBorderColor = Color(0xFF262626),
        dayInSelectionRangeContainerColor = Color(0xFF262626).copy(alpha = 0.1f),
        dayInSelectionRangeContentColor = Color(0xFF262626),
        dividerColor = Color.Transparent
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val start = state.selectedStartDateMillis?.let { 
                        java.time.Instant.ofEpochMilli(it).atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                    } ?: initialStart
                    val end = state.selectedEndDateMillis?.let { 
                        java.time.Instant.ofEpochMilli(it).atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                    } ?: initialEnd
                    onRangeSelected(start, end)
                },
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 12.dp)
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF262626)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Select Range",
                    color = Color.White,
                    fontFamily = plusJak,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        },
        shape = RoundedCornerShape(28.dp),
        colors = pickerColors,
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.padding(bottom = 12.dp, end = 8.dp)
            ) {
                Text("Cancel",
                    color = Color(0xFF6B7280),
                    fontFamily = plusJak,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    ) {
        Column(modifier = Modifier.padding(top = 16.dp)) {
            DateRangePicker(
                state = state,
                modifier = Modifier.weight(1f),
                colors = pickerColors,
                title = {
                    Text(
                        text = "Filter by range",
                        modifier = Modifier.padding(start = 24.dp, top = 16.dp),
                        fontFamily = plusJak,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF262626)
                    )
                },
                headline = {
                    DateRangePickerDefaults.DateRangePickerHeadline(
                        selectedStartDateMillis = state.selectedStartDateMillis,
                        selectedEndDateMillis = state.selectedEndDateMillis,
                        displayMode = state.displayMode,
                        dateFormatter = DatePickerDefaults.dateFormatter(),
                        modifier = Modifier.padding(start = 24.dp, bottom = 12.dp,top = 5.dp)
                    )
                },
                showModeToggle = false
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AttendanceCalendarCard(
    attendanceHistory: List<ClassSchedule>,
    startDate: LocalDate,
    endDate: LocalDate
) {
    val accentColor = Color(0xFF262626)
    var currentMonth by remember(endDate) { mutableStateOf(YearMonth.from(endDate)) }
    
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
        AnimatedContent(
            targetState = currentMonth,
            transitionSpec = {
                if (targetState > initialState) {
                    (slideInHorizontally { width -> width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> -width } + fadeOut())
                } else {
                    (slideInHorizontally { width -> -width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> width } + fadeOut())
                }
            }
        ) { targetMonth ->
            Text(
                text = targetMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = plusJak,
                color = Color(0xFF262626)
            )
        }
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
                            
                            val isWithinRange = !date.isBefore(startDate) && !date.isAfter(endDate)
                            val attendance = if (isWithinRange) attendanceMap[dateString] else null
                            
                            val targetBgColor = when(attendance?.attendanceStatus) {
                                1 -> Color(0xFF77BB7E) // Present - Green
                                2 -> Color(0xF3F24D4D) // Absent - Red
                                else -> Color.Transparent
                            }
                            val animatedBgColor by animateColorAsState(
                                targetValue = targetBgColor,
                                animationSpec = tween(durationMillis = 400)
                            )

                            val targetTextColor = when {
                                attendance != null -> Color.White
                                !isWithinRange -> Color.LightGray.copy(alpha = 0.8f)
                                else -> Color(0xFF4B5563)
                            }
                            val animatedTextColor by animateColorAsState(
                                targetValue = targetTextColor,
                                animationSpec = tween(durationMillis = 400)
                            )

                            val scale by animateFloatAsState(
                                targetValue = if (isWithinRange) 1f else 0.9f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                            )

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                        alpha = if (isWithinRange) 1f else 0.6f
                                    }
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(animatedBgColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day,
                                    fontSize = 16.sp,
                                    fontFamily = plusJak,
                                    color = animatedTextColor,
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RangeSummarySection(rate: String, attended: String, missed: String, total: String, dateRangeText: String, onEditRange: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF3F4F6))
                .clickable { onEditRange() }
                .padding(horizontal = 16.dp, vertical = 12.dp),
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
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            RangeSummaryItem(
                modifier = Modifier.weight(1f),
                label = "RATE",
                value = rate,
                backgroundColor = Color(0xFFE2DFFB),
                textColor = Color(0xFF1A1A1A),
                shape = RoundedCornerShape(20.dp, 5.dp, 5.dp, 20.dp)
            )
            RangeSummaryItem(
                modifier = Modifier.weight(1f),
                label = "PRESENT",
                value = attended,
                backgroundColor = Color(0xFFDBF8EB),
                textColor = Color(0xFF1A1A1A),
                shape = RoundedCornerShape(5.dp)

            )
            RangeSummaryItem(
                modifier = Modifier.weight(1f),
                label = "ABSENT",
                value = missed,
                backgroundColor = Color(0xFFF9E0E0),
                textColor = Color(0xFF1A1A1A),
                shape = RoundedCornerShape(5.dp, 20.dp, 20.dp, 5.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateRangeFilterPresets(
    minDate: LocalDate,
    onRangeSelected: (LocalDate, LocalDate) -> Unit,
    currentStart: LocalDate,
    currentEnd: LocalDate
) {
    val today = LocalDate.now()
    val options = listOf(
        "7D" to today.minusDays(7),
        "1M" to today.minusMonths(1),
        "3M" to today.minusMonths(3),
        "ALL" to minDate
    )

    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { (label, start) ->
            val isSelected = currentStart == start && currentEnd == today
            val animatedColor by animateColorAsState(if (isSelected) Color(0xFF262626) else Color(0xFFF3F4F6))
            val animatedContentColor by animateColorAsState(if (isSelected) Color.White else Color.Gray)
            
            Surface(
                onClick = { onRangeSelected(start, today) },
                shape = RoundedCornerShape(12.dp),
                color = animatedColor,
                contentColor = animatedContentColor,
                modifier = Modifier.height(36.dp).weight(1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold,
                        fontFamily = plusJak)
                }
            }
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
        AnimatedContent(
            targetState = value,
            transitionSpec = {
                (slideInVertically { height -> height } + fadeIn() togetherWith
                        slideOutVertically { height -> -height } + fadeOut())
            }
        ) { targetValue ->
            Text(
                text = targetValue,
                fontSize = 26.sp,
                fontFamily = plusJak,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
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
            AnimatedContent(
                targetState = title,
                transitionSpec = {
                    (fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                            scaleIn(initialScale = 0.92f, animationSpec = tween(220, delayMillis = 90)))
                        .togetherWith(fadeOut(animationSpec = tween(90)))
                }
            ) { targetTitle ->
                Text(
                    text = targetTitle,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = plusJak,
                    color = PrimaryColor,
                    modifier = Modifier.padding(4.dp)
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            AnimatedContent(
                targetState = subtitle,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(90))
                }
            ) { targetSubtitle ->
                Text(
                    text = targetSubtitle,
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    fontFamily = plusJak,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .padding(5.dp)
                )
            }
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

        AnimatedContent(
            targetState = predictionText,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
            }
        ) { targetText ->
            Text(
                text = targetText,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = plusJak,
                color = Color.White,
                lineHeight = 28.sp
            )
        }

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

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DateRangePickerDialogPreview() {
        DateRangePickerDialog(
            initialStart = LocalDate.now(),
            initialEnd = LocalDate.now().plusDays(7),
            onDismiss = {},
            onRangeSelected = { _, _ -> }
        )
}

