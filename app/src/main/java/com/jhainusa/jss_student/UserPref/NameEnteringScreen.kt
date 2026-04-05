package com.jhainusa.jss_student.UserPref

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jhainusa.jss_student.R
import com.jhainusa.jss_student.plusJak
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun UserInfoScreen(
    navController: NavController,
) {
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val name = UserPreferences.getName(context).first()
        val destination = if(name.isNullOrEmpty())"name_input" else "AllScreenNav"
        navController.navigate(destination){
            popUpTo(0)
        }
    }
}

@Composable
fun NameInputScreen(viewModel: NameViewModel = viewModel(),
                    navController: NavController) {
    var nameInput by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
            .imePadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        item {
            Text(
                text = "Hello there!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.plusjakartasansbold))
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "What should we call you?",
                fontSize = 16.sp,
                color = Color.Gray,
                fontFamily = plusJak
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = nameInput,
                onValueChange = { nameInput = it },
                placeholder = {
                    Text(
                        "Enter your name",
                        fontFamily = plusJak
                    )
                },
                textStyle = TextStyle(
                    fontFamily = plusJak,
                    fontSize = 16.sp,
                ),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x52E5E5E5), RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0x74E5E5E5),
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.saveName(nameInput)
                    navController.navigate("onboarding"){
                        popUpTo(0)
                    }
                },
                enabled = nameInput.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF2E2E2E), // Dark button
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Continue", fontSize = 16.sp, fontFamily = plusJak)
            }
        }
    }
}