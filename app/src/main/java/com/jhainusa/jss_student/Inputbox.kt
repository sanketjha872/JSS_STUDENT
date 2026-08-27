package com.jhainusa.jss_student

import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp

@Composable
fun inputBox(headline : String,
             value : String, onValueChange : (String) -> Unit){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(headline, fontFamily = plusJak, color = MaterialTheme.colorScheme.onBackground) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = OutlineColor,
            cursorColor = Color.Gray
        ),
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.primary,
            fontFamily = plusJak,
            fontWeight = FontWeight.SemiBold
        )

    )
    Spacer(modifier = Modifier.height(8.dp))
}
