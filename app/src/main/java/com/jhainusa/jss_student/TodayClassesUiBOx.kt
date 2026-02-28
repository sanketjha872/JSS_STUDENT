package com.jhainusa.jss_student

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhainusa.jss_student.UserPref.UserPreferences
import com.jhainusa.jss_student.ui.theme.black1a

val plusJak = FontFamily(
        Font(R.font.plus_jakarta)
    )


@Preview
@Composable
fun FullPAge(
    ){
    val context = LocalContext.current
    val nameFlow = remember { UserPreferences.getName(context) }
    val name by nameFlow.collectAsState(initial = "Unknown")
    LazyColumn(
        modifier=  Modifier.fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
        ) {
        item {
            name(name)
        }
        item{
            check()
        }
        item {
            tc()
        }
    }
}

@Composable
fun name(
    name: String
){
    Text(
        text = "Nice Streak,\n$name",
        fontSize = 30.sp,
        color = Color(0xFF262626),
        fontFamily = FontFamily(Font(R.font.plusjakartasansbold))

    )
}
@Preview
@Composable
fun tc(){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFF5F4F4))
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Today classes",
                fontFamily = FontFamily(Font(R.font.plusjakartasansbold)),
                fontWeight = FontWeight.Bold,
                color = black1a,
                fontSize = 19.sp
            )
            Box(
                modifier = Modifier.padding(5.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFFFFF))
                    .padding(8.dp)
            ){
                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_forward_24),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        classComp("10:45 AM","M Nagaraj","Universal Human values",
            painterResource(R.drawable.baseline_code_24),
            color = Color(0xF8DFECDE), onColorChange = {}
        )
        classComp("11:45 AM","SKV","Operating System",
            painterResource(R.drawable.baseline_check_24),
            color = Color(0xFFded3fd),onColorChange = {}
        )
    }
}
@Composable
fun classComp(
    time : String = "",
    teacher : String,
    subject: String,
    icon : Painter,
    color: Color,
    onColorChange : () -> Unit
    ){

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(color)
            .clickable(
                onClick = {
                    onColorChange()
                }
            )
            .padding(horizontal = 18.dp,
                vertical = 20.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = subject,
                fontFamily = FontFamily(
                    Font(R.font.plusjakartasansmedium)
                ),
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = time+"\t\t\t"+teacher,
                fontFamily = plusJak,
                fontSize = 12.sp,
                color = Color.DarkGray
            )
        }
        Box(
            modifier = Modifier.padding(5.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(8.dp)
        ){
            Icon(
                painter = icon,
                contentDescription = null,

            )
        }
    }
    Spacer(modifier = Modifier.height(15.dp))

}

@Preview
@Composable
fun check(){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ){
        AttendancePerBox(
            "Total\nAttendance",
            "74%",
            "24 days in a row",
            Color(0XFFf8e9c8),
            Modifier.weight(1f)
        )

        AttendancePerBox(
            "Short\nAttendance",
            "55%",
            "High Alert",
            Color(0XFFdeecec),
            Modifier.weight(1f)
        )
    }
}

@Composable
fun AttendancePerBox(
    title : String ,
    per : String,
    streak : String,
    bg:Color,
    modifier: Modifier = Modifier
){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .padding(start = 24.dp, end = 34.dp,
                top = 26.dp, bottom = 26.dp)
    ) {
        Text(
            text = title,
            fontFamily = FontFamily(Font(R.font.plusjakartasansbold)),
            fontSize = 16.sp,
            color = black1a,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = per,
            fontFamily = FontFamily(Font(R.font.plusjakartasansbold)),
            fontSize = 34.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 2.dp)

        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = streak,
            fontFamily = plusJak,
            fontSize = 11.sp,
            color = Color.DarkGray,
            modifier = Modifier.clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(horizontal = 6.dp
                , vertical = 4.dp)
        )
    }
}