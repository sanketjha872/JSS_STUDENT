package com.jhainusa.jss_student

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhainusa.jss_student.RoomDatabase.MainVIewModel
import com.jhainusa.jss_student.RoomDatabase.Schedule
import com.jhainusa.jss_student.RoomDatabase.ScheduleDao
import com.jhainusa.jss_student.RoomDatabase.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeTable(vIewModel : MainVIewModel){
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    Column(
        modifier=  Modifier.fillMaxSize()
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.Absolute.spacedBy(9.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth()
        ) {
            Text(
                text = "Time Table",
                fontSize = 30.sp,
                color = Color(0xFF262626),
                fontFamily = FontFamily(Font(R.font.plusjakartasansbold)),
                modifier = Modifier.weight(1f)
            )
            Box(
                contentAlignment = Alignment.TopEnd
            ) {
                DropdownMenuExample(vIewModel)
            }


        }
        Spacer(modifier = Modifier.height(2.dp))
        monthChangeUi(
            currentMonth = currentMonth,
            onPreviousMonth = {currentMonth = currentMonth.minusMonths(1)},
            onNextMonth = {currentMonth = currentMonth.plusMonths(1)}
        )
        Spacer(modifier = Modifier.height(6.dp))
        
        CalendarWithExpandableView(
            currentMonth = currentMonth,
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.weight(1f)) {
            ScheduleTimeline(selectedDate, vIewModel)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun monthChangeUi(
    currentMonth : YearMonth,
    onPreviousMonth : () -> Unit,
    onNextMonth : () -> Unit
){
      Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 15.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(Color(247,247,247,1).copy(1f))
                .border(1.dp,
                    Color.LightGray.copy(0.4f),
                    RoundedCornerShape(13.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            IconButton(
                onClick = onPreviousMonth
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .clip(RoundedCornerShape(7.dp))
                        .background(Color.White)
                        .border(0.5.dp,Color.LightGray,RoundedCornerShape(7.dp))
                )
            }
            Text(
                text = "${currentMonth.month.name.lowercase().replaceFirstChar { it.uppercase() } +" "} ${ currentMonth.year}",
                color = Color(0xFF6B7280),
                fontFamily = FontFamily(Font(R.font.plusjakartasansmedium)),
                fontSize = 18.sp,
            )
            IconButton(
                onClick = onNextMonth
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .clip(RoundedCornerShape(7.dp))
                        .background(Color.White)
                        .border(0.5.dp,Color.LightGray,RoundedCornerShape(7.dp))
                )
            }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun TimeTablePreview() {
    // Preview is now complex due to ViewModel dependencies
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleTimeline(selectedDate: LocalDate, viewModel: MainVIewModel) {
    val schedules by viewModel.getAll().observeAsState(emptyList())
    
    val selectedDayName = selectedDate.dayOfWeek.name.lowercase()
        .replaceFirstChar { it.uppercase() }.take(3) // "Mon", "Tue", etc.

    val filteredSchedules by remember(schedules, selectedDayName) {
        derivedStateOf {
            schedules.flatMap { schedule ->
                schedule.scheduleday
                    .filter { it.day.startsWith(selectedDayName, ignoreCase = true) }
                    .map { daySchedule -> schedule to daySchedule }
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
    ) {
        if (filteredSchedules.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                    Text("No classes today", color = Color.Gray, fontFamily = plusJak)
                }
            }
        } else {
            items(filteredSchedules) { (schedule, daySchedule) ->
                val dateStr = selectedDate.toString()
                val attendance by viewModel.getAttendanceForDate(schedule.subjectId, dateStr)
                    .collectAsState(initial = null)
                
                ScheduleItemRow(
                    schedule = schedule, 
                    timing = daySchedule.timing,
                    attendanceStatus = attendance?.attendanceStatus ?: 0,
                    onStatusChange = { newStatus ->
                        viewModel.updateAttendance(
                            schedule.subjectId, 
                            dateStr, 
                            daySchedule.day, 
                            newStatus
                        )
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleItemRow(
    schedule: Schedule, 
    timing: String,
    attendanceStatus: Int,
    onStatusChange: (Int) -> Unit
) {
    val presentAction = SwipeAction(
        onSwipe = { onStatusChange(1) },
        icon = { Icon(painterResource(R.drawable.baseline_check_24), null, tint = Color.White, modifier = Modifier.padding(16.dp).size(24.dp)) },
        background = Color(0xFF4CAF50)
    )
    val absentAction = SwipeAction(
        onSwipe = { onStatusChange(2) },
        icon = { Icon(painterResource(R.drawable.cancel_svgrepo_com), null, tint = Color.White, modifier = Modifier.padding(16.dp).size(24.dp)) },
        background = Color(0xFFF44336)
    )

    val backgroundColor = when (attendanceStatus) {
        1 -> Color(0xFFE8F5E9) // Light green for Present
        2 -> Color(0xFFFFEBEE) // Light red for Absent
        else -> Color(schedule.color.toULong())
    }

    SwipeableActionsBox(
        startActions = listOf(presentAction),
        endActions = listOf(absentAction),
        swipeThreshold = 70.dp,
        backgroundUntilSwipeThreshold = Color.Transparent
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(backgroundColor)
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            val startTime = timing.split("-").firstOrNull()?.trim() ?: ""
            Column(modifier = Modifier.width(65.dp)) {
                Text(
                    text = startTime,
                    color = Color.DarkGray,
                    fontFamily = plusJak,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                if (attendanceStatus != 0) {
                    Text(
                        text = if (attendanceStatus == 1) "PRESENT" else "ABSENT",
                        color = if (attendanceStatus == 1) Color(0xFF2E7D32) else Color(0xFFC62828),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = schedule.subject,
                    fontFamily = FontFamily(Font(R.font.plusjakartasansmedium)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = schedule.teacher,
                    fontFamily = plusJak,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
            
            Icon(
                painter = painterResource(
                    when(attendanceStatus) {
                        1 -> R.drawable.baseline_check_24
                        2 -> R.drawable.cancel_svgrepo_com
                        else -> R.drawable.baseline_code_24
                    }
                ),
                contentDescription = null,
                tint = if (attendanceStatus == 0) Color.Gray else Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
