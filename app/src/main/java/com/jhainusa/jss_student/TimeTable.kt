package com.jhainusa.jss_student

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
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
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeTable(vIewModel : MainVIewModel){

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
                fontSize = 34.sp,
                fontFamily = FontFamily(Font(R.font.plusjakartasansbold)),
                modifier = Modifier.weight(1f)
            )
            Box(
                contentAlignment = Alignment.TopEnd
            ) {
                DropdownMenuExample(vIewModel)
            }


        }
        Spacer(modifier = Modifier.height(5.dp))
        showSchedule(vIewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun showSchedule(VIewModel: MainVIewModel){
    var state by remember { mutableStateOf(1) }
    dateandTime(day = state, ondaySelected = {state = it})
    Spacer(modifier = Modifier.height(5.dp))
    val t = if(state<10) "0"+state else state
    ScheduleTimeline(VIewModel,LocalDate.parse("2025-06-${t}").dayOfWeek.getDisplayName(
        TextStyle.SHORT,
        Locale.getDefault()))
}
@Composable
fun ScheduleTimeline(vIewModel: MainVIewModel,day : String) {

    val schedules by vIewModel.getbyDay(day).observeAsState(initial = emptyList())


    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
    ) {
        items(schedules) { item ->
            ScheduleItemRow(item)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ScheduleItemRow(item: Schedule) {

    val colorMap = remember { mutableStateMapOf<String, Color>() }
    val color = colorMap[item.time] ?: Color(0xFFD1D1D6)
    val present = SwipeAction(
        onSwipe = {
            colorMap[item.time] = Color(0xFFB2F2BB)
        },
        icon = {},
        background = Color.White
    )
    val absent = SwipeAction(
        onSwipe = {
            colorMap[item.time] = Color(0x94FC8383)
        },
        icon = {

        },
        background = Color.White
    )
    SwipeableActionsBox(
        startActions = listOf(present),
        endActions = listOf(absent),
        swipeThreshold = 70.dp,
        backgroundUntilSwipeThreshold = Color.White
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.time,
                modifier = Modifier.width(42.dp),
                color = Color.Gray,
                fontFamily = plusJak,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.width(30.dp))

            classComp(
                time = item.time,
                teacher = item.teacher, subject = item.subject,
                icon = painterResource(R.drawable.baseline_code_24),
                color = color,
                onColorChange = {
                    colorMap[item.time] = Color(0xFFD1D1D6)
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun dateandTime(day : Int, ondaySelected : (Int) -> Unit){
    val currdate = LocalDate.now()
    val date = currdate.dayOfMonth
    val daysinMonth = YearMonth.now().lengthOfMonth()
    val dates = (1..daysinMonth).toList()

        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 17.dp)
        ) {
            items(dates) { i ->
                val t =
                    if (i < 10) {
                        "0" + i.toString()
                    } else {
                        i.toString()
                    }

                val z = day == t.toInt()
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = LocalDate.parse("2025-06-${t}").dayOfWeek.getDisplayName(
                            TextStyle.SHORT,
                            Locale.getDefault()
                        ),
                        fontFamily = plusJak,
                        fontSize = 14.sp,
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (z) Color.DarkGray else Color.Transparent),
                        contentAlignment = Alignment.Center

                    ) {
                        Text(
                            text = t,
                            fontFamily = FontFamily(Font(R.font.plusjakartasansbold)),
                            fontSize = 17.sp,
                            color = if (z) Color.White else Color.Black,
                            modifier = Modifier.clickable {
                                 ondaySelected(i)
                            }.padding(8.dp)
                        )
                    }
                }
            }
    }
}

