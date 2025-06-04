package com.jhainusa.jss_student.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhainusa.jss_student.R
import com.jhainusa.jss_student.plusJak

@Preview
@Composable
fun bs(){
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Green),
        contentAlignment = Alignment.Center
    ){
        dropdown()
        DropdownDemo()
    }
}

@Composable
fun dropdown(){
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(15.dp),
        border = BorderStroke(0.5.dp,Color.LightGray)
    ) {
        Column(
            modifier = Modifier.width(205.dp).padding(15.dp)
        ) {
            comp("Present")
            comp("Absent")
        }
    }
}


@Composable
fun comp(text : String){
    var click by remember { mutableStateOf(false) }
      Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.clip(RoundedCornerShape(16.dp))
              .fillMaxWidth()
              .background(if(click)Color(0xFFE5E3E5) else Color.White)
              .clickable(
                  onClick = {
                      click = !click
                  }
              )
              .padding(15.dp)

      ) {
          Text(text = text,
              fontFamily = plusJak,
              fontWeight = FontWeight.W600,
              fontSize = 16.sp
          )
          if(click) {
              Icon(
                  painter = painterResource(R.drawable.check_circle_fill_svgrepo_com),
                  contentDescription = null,
                  modifier = Modifier.size(24.dp),
              )
          }
      }
    Spacer(modifier = Modifier.height(5.dp))
}
@Composable
fun CustomDropdown(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize()) {
        // The clickable text field / button
        Text(
            text = selectedItem,
            modifier = Modifier
                .background(Color.LightGray)
                .clickable { expanded = true }
                .padding(12.dp)
        )

        // The dropdown menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(15.dp)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun DropdownDemo() {
    var selected by remember { mutableStateOf("Select Item") }

    CustomDropdown(
        items = listOf("Apple", "Banana"),
        selectedItem = selected,
        onItemSelected = { selected = it }
    )
}

