package com.jhainusa.jss_student

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
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

    val firstOfMonth = currentMonth.atDay(1)
    val totalDays = currentMonth.lengthOfMonth()
    val firstDayOfWeek = (firstOfMonth.dayOfWeek.value % 7)

    val weeks = ((firstDayOfWeek + totalDays + 6) / 7)

    val rowHeight = 54.dp
    val headerHeight = 48.dp + 16.dp

    val expandedHeight = headerHeight + (weeks * rowHeight)

    val calendarHeight by animateDpAsState(
        targetValue = if (isExpanded) expandedHeight else 90.dp,
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
                .background(MaterialTheme.colorScheme.background)
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
                FullMonthCalendar(
                    currentMonth,
                    selectedDate,
                    weeks,
                    onDateSelected
                )
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
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { ondaySelected(date) }
            ) {
                Text(
                    text = date.dayOfWeek.name.take(3),
                    color = if (isSelected) MaterialTheme.colorScheme.onBackground else Color.Gray,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 12.sp,
                    fontFamily = plusJak,
                    )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.onBackground else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        fontFamily = plusJak,
                        color = if (isSelected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground
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
    weeks: Int,
    onDateSelected: (LocalDate) -> Unit
) {
    val daysOfWeek = listOf(
        DayOfWeek.SUNDAY,
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY
    )

    val firstOfMonth = yearMonth.atDay(1)
    val totalDays = yearMonth.lengthOfMonth()
    val firstDayOfWeek = (firstOfMonth.dayOfWeek.value % 7)

    Column(modifier = Modifier.padding(16.dp)) {

        // 🔥 Header (Days)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day.name.take(3),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontFamily = plusJak,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🔥 Prepare days list
        val days = mutableListOf<LocalDate?>()
        repeat(firstDayOfWeek) { days.add(null) }
        for (day in 1..totalDays) {
            days.add(yearMonth.atDay(day))
        }

        // 🔥 Render weeks dynamically
        for (week in 0 until weeks) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (day in 0..6) {
                    val index = week * 7 + day
                    val date = days.getOrNull(index)

                    val isSelected = date == selectedDate
                    val isToday = date == LocalDate.now()

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.onBackground else Color.Transparent
                            )
                            .clickable(enabled = date != null) {
                                date?.let { onDateSelected(it) }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (date != null) {
                            Text(
                                text = date.dayOfMonth.toString(),
                                color = when {
                                    isSelected -> MaterialTheme.colorScheme.background
                                    isToday -> MaterialTheme.colorScheme.onBackground
                                    else -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)
                                },
                                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp,
                                fontFamily = plusJak,
                                )
                        }
                    }
                }
            }
        }
    }
}