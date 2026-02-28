package com.jhainusa.jss_student

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun CustomFloatingDropdown() {
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val selectedDays = remember { mutableStateMapOf<String, Boolean>() }
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        days.forEach { day ->
            val isSelected = selectedDays[day] == true
            PopOutButtonDemo(
                text = day,
                selected = isSelected,
                onClick =
                    {
                        selectedDays[day] = !isSelected
                    }
            )
        }
    }
}

@Composable
fun PopOutButtonDemo(text: String,
                     selected : Boolean,
                     onClick: () -> Unit) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(25.dp),
            border = BorderStroke(0.5.dp,Color(0xFF262626)),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selected) Color(0xFF262626) else Color.White
            ),
            elevation = ButtonDefaults.elevatedButtonElevation(
                pressedElevation = 20.dp, focusedElevation = 10.dp
            ),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 11.dp)

        ) {
            Text(
                text = text,
                fontSize = 17.sp,
                fontFamily = plusJak,
                color = if (selected) Color.White else Color(0xFF262626)
            )
        }
}
