package com.jhainusa.jss_student

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhainusa.jss_student.RoomDatabase.MainVIewModel
import com.jhainusa.jss_student.RoomDatabase.Schedule
import kotlinx.coroutines.delay


@Composable
fun periodBox(mainVIewModel: MainVIewModel,selectedDay : String){
    var startTime by remember { mutableStateOf("") }
    var EndTime by remember { mutableStateOf("") }
    var Subject by remember { mutableStateOf("") }
    var Teacher by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    if (isLoading) {
        LaunchedEffect(Unit) {
            delay(2000)
            isLoading = false
        }
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth().border(
            1.15.dp,Color.LightGray, RoundedCornerShape(12.dp)
        )
            .padding(horizontal = 14.dp, vertical = 20.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)

            ){
                inputBox("Start Time","8:00",startTime,
                    onValueChange = {startTime = it})
            }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    inputBox("End Time","9:00",EndTime,
                        onValueChange = {EndTime = it})
                }
        }
        inputBox("Subject","Enter Subject name",
            Subject, onValueChange = {Subject = it})
        inputBox("Teacher","Enter Teacher name", Teacher,
            onValueChange = {Teacher = it})
        Button(
            onClick = {
                if(selectedDay!="" && Subject!="" && startTime!="" && EndTime!="") {
                    mainVIewModel.insertSchedule(
                        Schedule(
                            day = selectedDay,
                            subject = Subject,
                            time = startTime + "-" + EndTime,
                            teacher = Teacher
                        )
                    )
                    isLoading = true;
                }
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            ),
            elevation = ButtonDefaults.elevatedButtonElevation(
                pressedElevation = 10.dp, focusedElevation = 20.dp
            ),
            modifier = Modifier.fillMaxWidth(0.95f),
            contentPadding = PaddingValues(vertical = 14.dp)
        ) {

            if(!isLoading) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = if(isLoading)"Adding..." else "Add Time Slot",
                fontSize = 16.sp,
                fontFamily = plusJak,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(4.dp))
            if(isLoading){
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20
                        .dp)
                )
            }
        }
    }

}