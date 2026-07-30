package com.jhainusa.jss_student

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jhainusa.jss_student.UserPref.NameViewModel
import com.jhainusa.jss_student.ui.theme.SubtitleGray
import com.jhainusa.jss_student.ui.theme.black1a

@Preview
@Composable
fun MoreOptionsScreen(
    nameViewModel: NameViewModel = viewModel(),
    onBackClick: () -> Unit = {}
) {
    val notificationsEnabled by nameViewModel.notificationsEnabledFlow.collectAsState()
    var showFeedbackDialog by remember { mutableStateOf(false) }
    var feedbackType by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        AnalyticsHelper.logScreenView("MoreOptions", "MoreOptions")
    }

    Scaffold(
        containerColor = Color.White,
        modifier = Modifier.background(Color.White).statusBarsPadding(),
        topBar = { MoreOptionsTopBar(onBackClick) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Heading
            item {
                Text(
                    text = "Settings",
                    fontSize = 36.sp,
                    fontFamily = FontFamily(Font(R.font.plusjakartasansbold)),
                    fontWeight = FontWeight.Bold,
                    color = black1a,
                    lineHeight = 40.sp
                )
            }

            // Premium Card
            item {
                PremiumCard()
            }

            // Support Section
            item {
                SettingsSection(
                    title = "Support",
                    items = listOf(
                        MoreOptionItem("Report a Bug", onClick = {
                            feedbackType = "Bug Report"
                            showFeedbackDialog = true
                        }),
                        MoreOptionItem("Suggest a Feature", onClick = {
                            feedbackType = "Feature Suggestion"
                            showFeedbackDialog = true
                        }),
                        MoreOptionItem("Contact Support", onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:sanketjha116@gmail.com")
                                putExtra(Intent.EXTRA_SUBJECT, "Support: Schedo App")
                            }
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
                            }
                        }),
                        MoreOptionItem(
                            title = "Notifications",
                            onClick = { nameViewModel.setNotificationsEnabled(!notificationsEnabled) }
                        ) {
                            Switch(
                                checked = notificationsEnabled,
                                onCheckedChange = { nameViewModel.setNotificationsEnabled(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Color(0xFF262626)
                                )
                            )
                        }
                    ),
                    bgColor = Color(0xFFF0FBF5)
                )
            }

            // About Schedo Section
            item {
                SettingsSection(
                    title = "About Schedo",
                    items = listOf(
                        MoreOptionItem("Rate Schedo", onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.jhainusa.jss_student"))
                            context.startActivity(intent)
                        }),
                        MoreOptionItem("Check for Updates", onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.jhainusa.jss_student"))
                            context.startActivity(intent)
                        }),
                        MoreOptionItem("Privacy Policy", onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sanketjha872.github.io/schedo-privacy-policy/"))
                            context.startActivity(intent)
                        }),
                        MoreOptionItem("Share App", onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "Check out Schedo, an amazing app for managing your attendance! https://play.google.com/store/apps/details?id=com.jhainusa.jss_student")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Schedo via"))
                        })
                    ),
                    bgColor = Color(0xFFFCF1F1)
                )
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }

    if (showFeedbackDialog) {
        FeedbackDialog(
            type = feedbackType,
            onDismiss = { showFeedbackDialog = false },
            onSubmit = { message ->
                nameViewModel.sendFeedback(
                    type = feedbackType,
                    message = message,
                    onSuccess = {
                        Toast.makeText(context, "Feedback sent! Thank you.", Toast.LENGTH_SHORT).show()
                        showFeedbackDialog = false
                    },
                    onError = { error ->
                        Toast.makeText(context, "Failed to send: $error", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        )
    }
}

@Composable
fun FeedbackDialog(
    type: String,
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    var message by remember { mutableStateOf("") }

    AnimatedDialog(showDialog = true, onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
        ) {
            Text(
                text = type,
                fontSize = 20.sp,
                fontFamily = plusJak,
                fontWeight = FontWeight.Bold,
                color = black1a
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = message,
                onValueChange = { message = it },
                placeholder = { Text("Tell us more...", fontFamily = plusJak) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                shape = RoundedCornerShape(12.dp),
                textStyle = TextStyle(fontFamily = plusJak, fontSize = 16.sp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onSubmit(message) },
                enabled = message.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF262626),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Submit Feedback", fontFamily = plusJak, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun MoreOptionsTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 8.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back",
                modifier = Modifier.size(20.dp),
                tint = black1a
            )
        }
    }
}

@Composable
fun PremiumCard() {
    val context = LocalContext.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { 
                Toast.makeText(context, "Premium features coming soon!", Toast.LENGTH_SHORT).show()
            },
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFFEDF3F8),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFF262626), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Premium",
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.plusjakartasansmedium)),
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF323131)

                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Explore all features",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.plusjakartasansmedium)),
                    fontWeight = FontWeight.SemiBold,
                    color = SubtitleGray
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun SettingsSection(title: String, items: List<MoreOptionItem>, bgColor: Color) {
    Column {
        Text(
            text = title,
            fontSize = 15.sp,
            fontFamily = plusJak,
            fontWeight = FontWeight.Medium,
            color = SubtitleGray,
            modifier = Modifier.padding(start = 8.dp, bottom = 14.dp)
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = bgColor,
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    SettingsRow(item = item)
                    if (index < items.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            thickness = 1.5.dp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsRow(item: MoreOptionItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { item.onClick() }
            .padding(horizontal = 20.dp, vertical = if (item.trailingContent != null) 10.dp else 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = item.title,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.plusjakartasansmedium)),
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF323131)
        )
        item.trailingContent?.invoke()
    }
}

data class MoreOptionItem(
    val title: String,
    val onClick: () -> Unit,
    val trailingContent: @Composable (() -> Unit)? = null
)
