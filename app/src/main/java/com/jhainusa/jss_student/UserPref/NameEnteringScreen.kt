package com.jhainusa.jss_student.UserPref

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jhainusa.jss_student.plusJak

@Composable
fun UserInfoScreen(
    navController: NavController,
    viewModel: NameViewModel = viewModel()
) {
    val name by viewModel.nameFlow.collectAsState()

    LaunchedEffect(name) {
        if (!name.isNullOrEmpty() && name != "Unknown") {
            navController.navigate("AllScreenNav") {
                popUpTo("userinfo") { inclusive = true } // remove from back stack
            }
        }
    }

    if (name == null || name == "Unknown") {
        NameInputScreen(onContinue = {
            viewModel.saveName(it)
        })
    }
}

    @Composable
    fun NameInputScreen(onContinue: (String) -> Unit) {
        var nameInput by remember { mutableStateOf("") }
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x64E5E5E5))
                .padding(32.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Text(
                text = "Hello there!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = plusJak
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
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    onContinue(nameInput)
                },
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

