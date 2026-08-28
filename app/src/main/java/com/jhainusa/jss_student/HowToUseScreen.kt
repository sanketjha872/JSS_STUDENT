package com.jhainusa.jss_student

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun HowToUseScreen(
    onDismiss: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxSize().padding(top = 2.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Top Bar with Drag Handle and Close Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp)
            ) {
                // Drag Handle
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFFE0E0E0))
                        .align(Alignment.TopCenter)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "How to use Time Table",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.plusjakartasansbold)),
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Mark attendance and manage your classes easily.",
                fontSize = 15.sp,
                fontFamily = plusJak,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                item {
                    InstructionItem(
                        imageRes = R.drawable.presentmarkimg,
                        placeholderColor = Color.Gray, // Light Green
                        title = "Swipe Right for Present",
                        description = "Swipe any class card to the right to mark it as present."
                    )
                }
                item {
                    InstructionItem(
                        imageRes = R.drawable.absentmarkimg,
                        placeholderColor = Color(0xFFFFEBEE), // Light Red
                        title = "Swipe Left for Absent",
                        description = "Swipe any class card to the left to mark it as absent."
                    )
                }
                item {
                    InstructionItem(
                        imageRes = R.drawable.holidaymarkimg,
                        placeholderColor = Color(0xFFE3F2FD), // Light Blue
                        title = "More Swipe Left for Holiday",
                        description = "Swipe further to the left to mark the class as holiday."
                    )
                }
                item {
                    InstructionItem(
                        imageRes = R.drawable.unmarkimg,
                        placeholderColor = Color(0xFFF3E5F5), // Light Purple
                        title = "Tap to Unmark",
                        description = "Tap on any class card to remove its attendance (unmark)."
                    )
                }
                item {
                    InstructionItem(
                        imageRes = R.drawable.addextraclassimg,
                        placeholderColor = Color(0xFFFFF3E0), // Light Orange
                        title = "Add Extra Class",
                        description = "Tap the + icon at the top right to add an extra class."
                    )
                }
                item {
                    InstructionItem(
                        imageRes = R.drawable.datesrowimg,
                        placeholderColor = Color(0xFFF1F8E9), // Light Lime
                        title = "Scroll Dates",
                        description = "Scroll the dates row left or right to mark and view other days."
                    )
                }
                item {
                    InstructionItem(
                        imageRes = R.drawable.fullcalendarimg,
                        placeholderColor = Color(0xFFEFEBE9), // Light Brown
                        title = "Full Calendar View",
                        description = "Drag the calendar header down to open the full calendar view.",
                        height = 150.dp
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp, top = 16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Got it",
                    color = MaterialTheme.colorScheme.background,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = plusJak
                )
            }
        }
    }
}

@Composable
fun InstructionItem(
    imageRes: Int?,
    placeholderColor: Color = Color(0xFFF9F9F9),
    title: String,
    description: String,
    height : Dp = 65.dp
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Image Container
        Box(
            modifier = Modifier
                .size(width = 175.dp, height = height)
                .clip(RoundedCornerShape(12.dp))
                .background(placeholderColor),
            contentAlignment = Alignment.Center
        ) {
            if (imageRes != null) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = "IMAGE",
                    fontSize = 10.sp,
                    color = Color.Gray.copy(alpha = 0.5f),
                    fontFamily = plusJak,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.width(20.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.plusjakartasansbold)),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                fontFamily = plusJak,
                color = Color.Gray,
                lineHeight = 18.sp
            )
        }
    }
}
