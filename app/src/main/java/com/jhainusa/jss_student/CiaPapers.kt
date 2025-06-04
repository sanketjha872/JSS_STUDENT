package com.jhainusa.jss_student

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Preview
@Composable
fun Papers(){

    val list = arrayOf(
        years("BTech 1st Year", Color(0xFFeef5db)),
        years("BTech 2nd Year", Color(0xFFE9DCE5)),
        years("BTech 3rd Year", Color(0xFFfcefe3)),
        years("BTech 4th Year", Color(0xFFDCEAEA)),
        years("MCA 1st Year", Color(0xFFE9DCE5)),
        years("MCA 2nd Year", Color(0xFFfcefe3)),
        years("MCA 3rd Year", Color(0xFFDCEAEA))
    )
            Column(
                verticalArrangement = Arrangement.Absolute.spacedBy(9.dp),
                horizontalAlignment = Alignment.Start,
               modifier = Modifier.fillMaxSize()
                   .background(Color.White)
                   .padding(20.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_up_svgrepo_com),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )

                    Text(
                        text = "Exam\nPapers of CIA",
                        fontSize = 35.sp,
                        fontFamily = FontFamily(Font(R.font.plusjakartasansbold))
                    )
                SBar(
                    "",
                    onQueryChange = {}
                )
                Spacer(modifier = Modifier.height(5.dp))
                    LazyColumn(

                    ) {
                        items(list){
                            Years(it.year,it.color)
                        }
                    }
            }
}
@Composable
fun SBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search.."
) {
    val state by remember { mutableStateOf("") }
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(placeholder, fontFamily = plusJak, fontWeight = FontWeight.W500) },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search Icon")
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFF1F1F3),
            focusedContainerColor = Color(0xFFF1F1F3),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}

@Composable
fun Years(
    year : String,
    color: Color
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(color)
            .padding(horizontal = 18.dp,
                vertical = 20.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = year,
                fontFamily = FontFamily(
                    Font(R.font.plusjakartasansmedium)
                ),
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "1st & 2nd Sem",
                fontFamily = plusJak,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        Box(
            modifier = Modifier.padding(5.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(8.dp)
        ){
            Icon(
                painter = painterResource(R.drawable.arrow_sm_right_svgrepo_com),
                contentDescription = null,

                )
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}
