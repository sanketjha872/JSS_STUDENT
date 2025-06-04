package com.jhainusa.jss_student

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jhainusa.jss_student.RoomDatabase.MainVIewModel
import com.jhainusa.jss_student.RoomDatabase.Schedule
import java.nio.file.WatchEvent

@Composable
fun chck(mainVIewModel: MainVIewModel){
    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(20.dp)

        ,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
           Icon(
               painter = painterResource(
                   R.drawable.arrow_up_svgrepo_com
               ),
               contentDescription = null,
               modifier = Modifier.size(30.dp)
           )
           Spacer(modifier = Modifier.height(5.dp))
           UpperBox()
           ScheduleScreen(mainVIewModel)
           Spacer(modifier = Modifier.height(20.dp))
       }
}
@Composable
fun ScheduleScreen(mainVIewModel: MainVIewModel) {
    var selectedDay by remember { mutableStateOf("Mon") }

    Column {
        Daylist(selectedDay = selectedDay, onDaySelected = { selectedDay = it })
        periodBox(mainVIewModel,selectedDay)
    }
}
@Composable
fun Daylist(
    selectedDay : String,
    onDaySelected : (String) -> Unit){
    val days = arrayOf("Mon","Tue","Wed","Thu","Fri","Sat")
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray)
            .height(1.dp)
    )
    LazyRow(
        modifier = Modifier.fillMaxWidth()
            .padding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        items(days){ item->
            val z = selectedDay == item
                Text(
                    text = item,
                    fontFamily = plusJak,
                    fontSize = 13.5.sp,
                    modifier = Modifier
                        .background(
                            if(z) Color.Black else Color.Transparent
                        )
                        .padding(
                            horizontal = 14.dp,
                            vertical = 14.dp
                        ).clickable(
                            onClick = {
                                onDaySelected(item)
                            }
                        ),
                    color = if(z) Color.White else Color.Black
                )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray)
            .height(1.dp)
    )
    Spacer(modifier = Modifier.height(20.dp))

}
@Composable
fun addTimeSlot(){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(1f)
            .clip( RoundedCornerShape(10.dp))
            .background(Color.Black)
            .padding(vertical = 14.dp)

    ) {
        Icon(
             imageVector = Icons.Default.Add,
             contentDescription = null,
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Add Time Slot",
            fontSize = 16.sp,
            fontFamily = plusJak,
            color = Color.White
        )
    }
}
@Composable
fun UpperBox(){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
            .padding(
                start = 5.dp,
                end = 9.dp
            )
    ) {
        Text(
            text = "Class\nSchedule",
            fontFamily = FontFamily(Font(R.font.plusjakartasansbold)),
            fontSize = 35.sp
        )
        Text(
            text = "\n\nSave",
            fontFamily = plusJak,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
}