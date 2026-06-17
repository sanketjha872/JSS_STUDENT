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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jhainusa.jss_student.AnalyticsHelper
import com.jhainusa.jss_student.R
import com.jhainusa.jss_student.SkeletonYearItem
import com.jhainusa.jss_student.plusJak
import com.jhainusa.jss_student.ui.theme.black1a


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Papers(navController: NavController) {
    val viewModel: PapersViewModel = viewModel()
    val list by viewModel.years.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadyears()
        AnalyticsHelper.logScreenView("CiaPapersScreen", "Papers")
    }

    val scrollState = rememberLazyListState()

        LazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                CollapsingHeader(scrollState)
            }

            if (isLoading) {
                items(5) {
                    SkeletonYearItem()
                }
            }
            else{
            items(list) { yearId ->
                Years(year = yearId) {
                    AnalyticsHelper.logEvent("select_year", android.os.Bundle().apply {
                        putString("year_id", yearId)
                    })
                    navController.navigate("semesters/$yearId")
                }
            }
                }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PapersPreview() {
    Papers(navController = NavController(androidx.compose.ui.platform.LocalContext.current))
}

@Composable
fun CollapsingHeader(scrollState: LazyListState) {
    var searchPaper by remember { mutableStateOf("") }
    val maxOffset = 200f
    val offset = minOf(
        scrollState.firstVisibleItemScrollOffset.toFloat(),
        maxOffset
    )

    val scale = 1f - (offset / maxOffset) * 0.2f
    val alpha = 1f - (offset / maxOffset) * 0.5f
    val translateY = -(offset / 2)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationY = translateY
                translationX = translateY
                this.alpha = alpha
            }
            .background(Color.White)
            .padding(20.dp),
        verticalArrangement = Arrangement.Absolute.spacedBy(9.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        /*Icon(
            painter = painterResource(R.drawable.arrow_up_svgrepo_com),
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )*/

        /*Text(
            text = "Exam\nPapers of CIA",
            fontSize = 35.sp,
            color = Color(0xFF262626),
            fontFamily = FontFamily(Font(R.font.plusjakartasansbold))
        )*/
        Text(
            text = "Exam Papers",
            color = Color(0xFF1A1A1A),
            fontFamily = FontFamily(Font(R.font.plusjakartasansbold)),
            fontSize = 30.sp,
        )
        Text(
            text = "Internal College Papers",
            color = Color(0xFF6B7280),
            fontFamily = FontFamily(Font(R.font.plusjakartasansregular)),
            fontSize = 18.sp,
        )
        Spacer(modifier = Modifier.height(5.dp))
        SBar(searchPaper, onQueryChange = {searchPaper = it})
    }
}

@Composable
fun SBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search.."
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(placeholder, fontFamily = plusJak, fontWeight = FontWeight.W500) },
        leadingIcon = {
            Icon(painterResource(R.drawable.search), contentDescription = "Search Icon",
                modifier = Modifier.size(20.dp))
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        textStyle = TextStyle(
            fontFamily = plusJak, // custom font
            fontSize = 16.sp,
        ),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFF1F1F3),
            focusedContainerColor = Color(0xFFF1F1F3),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = Color.Gray
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
            .padding(horizontal = 18.dp)
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
