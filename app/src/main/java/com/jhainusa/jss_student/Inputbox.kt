package com.jhainusa.jss_student

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun inputBox(headline : String,placehold : String,
             value : String, onValueChange : (String) -> Unit){
    Column {
        Text(
            text = headline,
            fontSize = 14.sp,
            fontFamily = plusJak,
        )
        Spacer(modifier = Modifier.height(5.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placehold,
                    fontFamily = plusJak,
                    fontWeight = FontWeight.W500,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.LightGray,
                unfocusedBorderColor = Color.LightGray
            ),
            shape = RoundedCornerShape(14.dp),
        )
    }
}
