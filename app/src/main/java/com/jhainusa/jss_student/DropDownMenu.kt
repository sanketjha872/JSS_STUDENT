package com.jhainusa.jss_student

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jhainusa.jss_student.GeminiBackend.sendImageToSupabase
import com.jhainusa.jss_student.RoomDatabase.MainVIewModel
import com.jhainusa.jss_student.UserPref.NameViewModel

@Composable
fun DropdownMenuExample(
    vIewModel: MainVIewModel,
    isEditMode: Boolean,
    onEditModeToggle: () -> Unit,
    nameViewModel: NameViewModel = viewModel() // Use NameViewModel to get userId
) {
    val context = LocalContext.current
    val userId by nameViewModel.userIdFlow.collectAsState()
    var loading by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            loading = true
            // Pass the real userId retrieved from DataStore
            sendImageToSupabase(
                context = context,
                uri = it,
                userIdStr = userId ?: "unknown_user",
                viewModel = vIewModel
            ) { success ->
                loading = false
                Log.d("Supabase", "Success: $success")
            }
        }
    }

    IconButton(
        onClick = {
            if (isEditMode) {
                onEditModeToggle()
            } else {
                expanded = !expanded
            }
        }
    ) {
        Icon(
            painter = painterResource(if (isEditMode) R.drawable.baseline_check_24 else R.drawable.upload_square_svgrepo_com),
            contentDescription = if (isEditMode) "Done" else "More Options",
            tint = if (isEditMode) Color(0xFF2E7D32) else Color(0xFF6B7280),
            modifier = Modifier.size(29.dp)
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(onClick = {
            expanded = false
            launcher.launch("image/*")
        }) {
            Text(
                text = "Upload",
                color = Color.Black,
                fontSize = 15.sp,
                fontFamily = plusJak,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(30.dp))
            Icon(
                painter = painterResource(R.drawable.ai_svgrepo_com),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }
        DropdownMenuItem(onClick = {
            expanded = false
            onEditModeToggle()
        }) {
            Text(
                text = if (isEditMode) "Done" else "Edit",
                color = Color.Black,
                fontSize = 15.sp,
                fontFamily = plusJak,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(if (isEditMode) R.drawable.baseline_check_24 else R.drawable.edit_svgrepo_com),
                contentDescription = null,
                tint = if (isEditMode) Color(0xFF2E7D32) else Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }
    }

    if (loading) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            LottieLoader("Analyzing your image...", R.raw.handloader)
        }
    }
}
