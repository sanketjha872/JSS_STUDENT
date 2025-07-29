package com.jhainusa.jss_student.ciaPaperPage

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jhainusa.jss_student.R
import com.jhainusa.jss_student.plusJak


@Preview
@Composable
fun Papers(){
    AppNavHost()
}

@Composable
fun Papers(navController: NavController){
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
                yearsShow(onYearSelected = {
                   navController.navigate("semesters/$it")
                })
            }
}


@Composable
fun yearsShow(
    onYearSelected: (String) -> Unit
) {
    val viewModel: PapersViewModel = viewModel() // Your ViewModel that has loadyears()
    val list by viewModel.years.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadyears()
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(list) { yearId ->
            Years(yearId, onClick = { onYearSelected(yearId) })
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
    onClick :() -> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable {
                onClick()
            }
            .background(Color(0xFFDCE4E9))
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
                text = "Even & Odd Sem",
                fontFamily = plusJak,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

            Icon(
                painter = painterResource(R.drawable.arrow_sm_right_svgrepo_com),
                contentDescription = null,
                modifier = Modifier.padding(5.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(8.dp)
            )
    }
    Spacer(modifier = Modifier.height(12.dp))
}
