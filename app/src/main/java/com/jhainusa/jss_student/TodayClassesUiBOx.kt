package com.jhainusa.jss_student

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhainusa.jss_student.RoomDatabase.MainVIewModel
import com.jhainusa.jss_student.UserPref.UserPreferences
import com.jhainusa.jss_student.ui.theme.black1a
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

val plusJak = FontFamily(
    Font(R.font.plus_jakarta)
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FullPAge(
    viewModel: MainVIewModel
) {
    val context = LocalContext.current
    val nameFlow = remember { UserPreferences.getName(context) }
    val name by nameFlow.collectAsState(initial = "Unknown")

    val subjectsList by viewModel.getAll().observeAsState(emptyList())

    val currentDay = remember {
        LocalDate.now().dayOfWeek.name.lowercase()
            .replaceFirstChar { it.uppercase() }.take(3) // "Mon", "Tue", etc.
    }

    val todayClasses by remember(subjectsList) {
        derivedStateOf {
            val now = LocalTime.now()
            subjectsList.flatMap { schedule ->
                schedule.scheduleday
                    .filter { it.day.startsWith(currentDay, ignoreCase = true) }
                    .map { daySchedule ->
                        TodayClassItem(
                            subject = schedule.subject,
                            teacher = schedule.teacher,
                            time = daySchedule.timing,
                            color = Color(schedule.color.toULong())
                        )
                    }
            }
            .filter { parseEndTime(it.time).isAfter(now) } // Only upcoming or ongoing
            .sortedBy { parseStartTime(it.time) }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        item {
            GreetingHeader(name)
        }
        item {
            AttendanceOverview()
        }
        item {
            TodayClassesSection(todayClasses)
        }
    }
}

data class TodayClassItem(
    val subject: String,
    val teacher: String,
    val time: String,
    val color: Color
)

@RequiresApi(Build.VERSION_CODES.O)
fun parseStartTime(timeRange: String): LocalTime {
    return try {
        val startTimeStr = timeRange.split("-").first().trim()
        val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
        LocalTime.parse(startTimeStr, formatter)
    } catch (e: Exception) {
        LocalTime.MIDNIGHT
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun parseEndTime(timeRange: String): LocalTime {
    return try {
        val endTimeStr = timeRange.split("-").last().trim()
        val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
        LocalTime.parse(endTimeStr, formatter)
    } catch (e: Exception) {
        LocalTime.MAX
    }
}

@Composable
fun GreetingHeader(
    name: String
) {
    Text(
        text = "Nice Streak,\n$name",
        fontSize = 30.sp,
        color = Color(0xFF262626),
        fontFamily = FontFamily(Font(R.font.plusjakartasansbold))
    )
}

@Composable
fun TodayClassesSection(classes: List<TodayClassItem>) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFF5F4F4))
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Today classes",
                fontFamily = FontFamily(Font(R.font.plusjakartasansbold)),
                fontWeight = FontWeight.Bold,
                color = black1a,
                fontSize = 19.sp
            )
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFFFFF))
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_forward_24),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))

        if (classes.isEmpty()) {
            Text(
                text = "No upcoming classes for today",
                fontFamily = plusJak,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 20.dp)
            )
        } else {
            classes.forEach { item ->
                classComp(
                    time = item.time,
                    teacher = item.teacher,
                    subject = item.subject,
                    icon = painterResource(R.drawable.baseline_code_24), // Default icon
                    color = item.color,
                    onColorChange = {}
                )
            }
        }
    }
}

@Composable
fun classComp(
    time: String = "",
    teacher: String,
    subject: String,
    icon: Painter,
    color: Color,
    onColorChange: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(color)
            .clickable(
                onClick = {
                    onColorChange()
                }
            )
            .padding(horizontal = 18.dp, vertical = 20.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = subject,
                fontFamily = FontFamily(
                    Font(R.font.plusjakartasansmedium)
                ),
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = time + "\t\t\t" + teacher,
                fontFamily = plusJak,
                fontSize = 12.sp,
                color = Color.DarkGray
            )
        }
        Box(
            modifier = Modifier
                .padding(5.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(8.dp)
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
            )
        }
    }
    Spacer(modifier = Modifier.height(15.dp))
}

@Preview
@Composable
fun AttendanceOverview() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AttendancePerBox(
            "Total\nAttendance",
            "74%",
            "24 days in a row",
            Color(0XFFf8e9c8),
            Modifier.weight(1f)
        )

        AttendancePerBox(
            "Short\nAttendance",
            "55%",
            "High Alert",
            Color(0XFFdeecec),
            Modifier.weight(1f)
        )
    }
}

@Composable
fun AttendancePerBox(
    title: String,
    per: String,
    streak: String,
    bg: Color,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .padding(
                start = 24.dp, end = 34.dp,
                top = 26.dp, bottom = 26.dp
            )
    ) {
        Text(
            text = title,
            fontFamily = FontFamily(Font(R.font.plusjakartasansbold)),
            fontSize = 16.sp,
            color = black1a,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = per,
            fontFamily = FontFamily(Font(R.font.plusjakartasansbold)),
            fontSize = 34.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 2.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = streak,
            fontFamily = plusJak,
            fontSize = 11.sp,
            color = Color.DarkGray,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(
                    horizontal = 6.dp, vertical = 4.dp
                )
        )
    }
}
