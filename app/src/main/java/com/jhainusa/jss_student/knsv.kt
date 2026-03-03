package com.jhainusa.jss_student

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jhainusa.jss_student.RoomDatabase.DaySchedule
import com.jhainusa.jss_student.RoomDatabase.MainVIewModel
import com.jhainusa.jss_student.RoomDatabase.Schedule
import java.util.Locale
import kotlin.math.absoluteValue

val PrimaryColor = Color(0xFF262626)
val SelectedDayColor = PrimaryColor
val UnselectedDayColor = Color.White
val OutlineColor = Color(0xFFE0E0E0)
val colorPalette = listOf(
    Color(0xFFEDE2FF), // Darker light lavender
    Color(0xFFFFE6E6), // Darker soft blush pink
    Color(0xFFFFF3CC), // Darker cream yellow
    Color(0xFFDFF7EC), // Darker soft mint
    Color(0xFFDFF7F7), // Darker powder aqua
    Color(0xFFFFE8D6), // Darker peach cream
    Color(0xFFE6F5EF), // Darker light teal mint
    Color(0xFFFFE6F2), // Darker pinkish white
    Color(0xFFDCE8FF), // Darker cloud blue
    Color(0xFFF5F5E6)
)

fun assignColor(name: String): Color {
    val index = name.hashCode().absoluteValue % colorPalette.size
    return colorPalette[index]
}


@Composable
fun AnimatedDialogContent(content: @Composable () -> Unit) {
    val transition = remember { MutableTransitionState(false).apply { targetState = true } }

    AnimatedVisibility(
        visibleState = transition,
        enter = fadeIn(animationSpec = tween(200)) + scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            initialScale = 0.7f
        ),
        exit = fadeOut(animationSpec = tween(150)) + scaleOut(
            animationSpec = tween(150),
            targetScale = 0.8f
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .background(Color.White, RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            content()
        }
    }
}

@Composable
fun AnimatedDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            AnimatedDialogContent {
                content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddClassScreen(viewModel: MainVIewModel, scheduleToEdit: Schedule? = null, onDimiss: () -> Unit) {
    val jakartaFont = plusJak

    val selectedDays = remember { 
        mutableStateMapOf<String, Pair<String, String>>().apply {
            scheduleToEdit?.scheduleday?.forEach {
                val times = it.timing.split(" - ")
                if (times.size == 2) {
                    put(it.day, times[0] to times[1])
                }
            }
        }
    }
    var subject by remember { mutableStateOf(scheduleToEdit?.subject ?: "") }
    var teacher by remember { mutableStateOf(scheduleToEdit?.teacher ?: "") }

    var showTimePicker by remember { mutableStateOf(false) }
    var currentPickingKey by remember { mutableStateOf<String?>(null) }
    var isPickingStartTime by remember { mutableStateOf(true) }

    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top 
    ) {
        item {
            Text(
                text = if (scheduleToEdit == null) "Add New Class" else "Edit Class",
                fontFamily = jakartaFont,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryColor
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            inputBox("Subject Name", subject, onValueChange = { subject = it })
        }

        item {
            inputBox("Teacher Name", teacher, onValueChange = { teacher = it })
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Text(
                text = "Select Days & Timings", 
                fontFamily = jakartaFont, 
                fontWeight = FontWeight.SemiBold, 
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(days.size) { index ->
            val day = days[index]
            val key = day
            val isSelected = selectedDays.containsKey(key)
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp) 
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) Color(0xFFF8F8F8) else Color.Transparent)
                    .border(
                        1.dp, 
                        if (isSelected) PrimaryColor.copy(0.1f) else OutlineColor, 
                        RoundedCornerShape(16.dp)
                    )
                    .clickable {
                        if (isSelected) {
                            selectedDays.remove(key)
                        } else {
                            selectedDays[key] = "09:00 AM" to "10:00 AM"
                        }
                    }
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) PrimaryColor else Color.White)
                            .border(1.dp, if (isSelected) PrimaryColor else OutlineColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day,
                            color = if (isSelected) Color.White else PrimaryColor,
                            fontFamily = jakartaFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Text(
                        text = if (isSelected) "Selected" else "Tap to select",
                        fontFamily = jakartaFont,
                        fontSize = 14.sp,
                        color = if (isSelected) PrimaryColor else Color.Gray,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (isSelected) {
                        Icon(
                            painter = androidx.compose.ui.res.painterResource(R.drawable.baseline_check_24),
                            contentDescription = null,
                            tint = PrimaryColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                if (isSelected) {
                    Spacer(modifier = Modifier.height(12.dp))
                    val (start, end) = selectedDays[key]!!
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TimeSelectionBox(
                            label = "Start",
                            time = start,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                currentPickingKey = key
                                isPickingStartTime = true
                                showTimePicker = true
                            }
                        )

                        TimeSelectionBox(
                            label = "End",
                            time = end,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                currentPickingKey = key
                                isPickingStartTime = false
                                showTimePicker = true
                            }
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
            val schedules = selectedDays.map { (day, value) ->
                DaySchedule(day, "${value.first} - ${value.second}")
            }
            Button(
                onClick = {
                    if (subject.isNotBlank()) {
                        viewModel.insertSchedule(
                            Schedule(
                                subjectId = scheduleToEdit?.subjectId ?: 0,
                                subject = subject,
                                teacher = teacher,
                                scheduleday = schedules,
                                color = scheduleToEdit?.color ?: assignColor(subject).value.toLong(),
                            )
                        )
                        onDimiss()
                    }
                },
                enabled = subject.isNotBlank() && selectedDays.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = PrimaryColor,
                    contentColor = Color.White,
                    disabledBackgroundColor = Color.LightGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.elevation(0.dp, 0.dp)
            ) {
                Text(
                    text = if (scheduleToEdit == null) "Save Class Schedule" else "Update Schedule", 
                    fontFamily = jakartaFont, 
                    color = Color.White, 
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(24.dp)) 
        }
    }

    if (showTimePicker && currentPickingKey != null) {
        val currentPair = selectedDays[currentPickingKey!!]!!
        val initialTimeStr = if (isPickingStartTime) currentPair.first else currentPair.second
        
        val hour = try { 
            var h = initialTimeStr.split(":")[0].toInt()
            if (initialTimeStr.contains("PM") && h < 12) h += 12
            if (initialTimeStr.contains("AM") && h == 12) h = 0
            h
        } catch (e: Exception) { 12 }
        
        val minute = try { initialTimeStr.split(":")[1].split(" ")[0].toInt() } catch (e: Exception) { 0 }

        TimePickerDialog(
            initialHour = hour,
            initialMinute = minute,
            onTimeSelected = { h, m ->
                val formatted = formatTime(h, m)
                val pair = selectedDays[currentPickingKey!!]!!
                if (isPickingStartTime) {
                    selectedDays[currentPickingKey!!] = formatted to pair.second
                } else {
                    selectedDays[currentPickingKey!!] = pair.first to formatted
                }
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }
}

@Composable
fun TimeSelectionBox(
    label: String,
    time: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(modifier = modifier.clickable { onClick() }) {
        Text(
            text = label, 
            fontSize = 12.sp, 
            color = Color.Gray, 
            fontFamily = plusJak,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(1.dp, OutlineColor, RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = androidx.compose.ui.res.painterResource(R.drawable.calendar_svgrepo_com),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = time,
                    fontFamily = plusJak,
                    fontSize = 14.sp,
                    color = PrimaryColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onTimeSelected: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberTimePickerState(initialHour = initialHour, initialMinute = initialMinute, is24Hour = false)
    
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White)
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Select Time",
                    fontFamily = plusJak,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.Start).padding(bottom = 20.dp)
                )
                
                TimePicker(state = state)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    androidx.compose.material3.TextButton(onClick = onDismiss) {
                        androidx.compose.material3.Text("Cancel", color = PrimaryColor)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    androidx.compose.material3.TextButton(
                        onClick = { onTimeSelected(state.hour, state.minute) }
                    ) {
                        androidx.compose.material3.Text("OK", color = PrimaryColor, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

fun formatTime(hour: Int, minute: Int): String {
    val amPm = if (hour < 12) "AM" else "PM"
    val h = if (hour % 12 == 0) 12 else hour % 12
    return String.format(Locale.getDefault(), "%02d:%02d %s", h, minute, amPm)
}
