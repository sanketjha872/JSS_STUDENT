package com.jhainusa.jss_student

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarWithExpandableView(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    val calendarHeight by animateDpAsState(
        targetValue = if (isExpanded) 330.dp else 90.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "CalendarHeight"
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(calendarHeight)
                .background(Color.White)
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onDragEnd = {
                            isExpanded = !isExpanded
                        },
                        onVerticalDrag = { _, _ -> }
                    )
                }
        ) {
            if (isExpanded) {
                FullMonthCalendar(currentMonth, selectedDate, onDateSelected)
            } else {
                DateStrip(currentMonth, selectedDate, onDateSelected)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateStrip(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    ondaySelected: (LocalDate) -> Unit
) {
    val today = LocalDate.now()
    val days = remember(currentMonth) {
        (1..currentMonth.lengthOfMonth()).map { day ->
            currentMonth.atDay(day)
        }
    }

    val listState = rememberLazyListState()

    LaunchedEffect(currentMonth) {
        val targetIndex = (today.dayOfMonth - 4).coerceAtLeast(0)
        listState.scrollToItem(targetIndex)
    }

    LazyRow(
        state = listState,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        items(days) { date ->
            val isSelected = selectedDate == date

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { ondaySelected(date) }
            ) {
                Text(
                    text = date.dayOfWeek.name.take(3),
                    color = if (isSelected) Color.Black else Color.Gray,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontFamily = plusJak,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            if (isSelected) Color(0xFF262626) else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        fontFamily = plusJak,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = if (isSelected) Color.White else Color.Black
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FullMonthCalendar(
    yearMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val daysOfWeek = DayOfWeek.values()
    val firstOfMonth = yearMonth.atDay(1)
    val lastOfMonth = yearMonth.atEndOfMonth()
    val firstDayOfWeek = (firstOfMonth.dayOfWeek.value % 7)

    val totalDays = lastOfMonth.dayOfMonth
    val weeks = ((firstDayOfWeek + totalDays + 6) / 7).toInt().coerceAtLeast(5)

    Column(modifier = Modifier.padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day.name.take(3),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium,
                    fontFamily = plusJak,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val days = mutableListOf<LocalDate?>()
        repeat(firstDayOfWeek) { days.add(null) }
        for (day in 1..totalDays) {
            days.add(yearMonth.atDay(day))
        }
        
        for (week in 0 until weeks) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                for (day in 0..6) {
                    val index = week * 7 + day
                    val date = days.getOrNull(index)
                    val isSelected = date != null && date == selectedDate
                    val isToday = date != null && date == LocalDate.now()
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(
                                color = if (isSelected) Color(0xFF262626) else Color.Transparent
                            )
                            .clickable(enabled = date != null) { date?.let { onDateSelected(it) } },
                        contentAlignment = Alignment.Center
                    ) {
                        if (date != null) {
                            Text(
                                text = date.dayOfMonth.toString(),
                                color = if (isSelected) Color.White else if (isToday) Color(0xFF262626) else Color.Black,
                                fontFamily = plusJak,
                                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
