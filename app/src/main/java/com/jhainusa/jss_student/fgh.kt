package com.jhainusa.jss_student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun make(){
    Card(
        elevation = 10.dp,
        shape = RoundedCornerShape(15.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(125.dp)
                .background(color = Color.White)
                .padding(horizontal = 8.dp)
        ) {
            TextButton(
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.LightGray
                ),
                onClick = {}
            ) {
                Text(
                    text = "Upload",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontFamily = plusJak,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(R.drawable.ai_svgrepo_com),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(17.dp)
                )
            }
            TextButton(
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.LightGray
                ),
                onClick = {}
            ) {
                Text(
                    text = "Edit",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontFamily = plusJak,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(R.drawable.edit_svgrepo_com),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(17.dp)
                )
            }
        }
    }
}