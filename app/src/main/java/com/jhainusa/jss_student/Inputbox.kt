package com.jhainusa.jss_student

import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*

import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp

@Composable
fun inputBox(headline : String,
             value : String, onValueChange : (String) -> Unit){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(headline, fontFamily = plusJak, color = PrimaryColor) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = PrimaryColor,
            unfocusedBorderColor = OutlineColor
        )
    )
    Spacer(modifier = Modifier.height(8.dp))
}
