package com.jhainusa.jss_student

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhainusa.jss_student.GeminiBackend.encodeImageToBase64
import com.jhainusa.jss_student.GeminiBackend.sendToGemini
import com.jhainusa.jss_student.RoomDatabase.DaySchedule
import com.jhainusa.jss_student.RoomDatabase.MainVIewModel
import com.jhainusa.jss_student.RoomDatabase.Schedule
import com.jhainusa.jss_student.ciaPaperPage.SBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UploadTimeTableScreen(viewModel: MainVIewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val subjectsList by viewModel.getAll().observeAsState(emptyList())
    var searchSubject by remember { mutableStateOf("") }
    var showAddSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    
    var selectedSubjectForHistory by remember { mutableStateOf<Schedule?>(null) }
    var showHistoryDialog by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(false) }
    var isAiLoading by remember { mutableStateOf(false) }
    var scheduleToEdit by remember { mutableStateOf<Schedule?>(null) }
    val globalLazyListState = rememberLazyListState()

    var isBottomBarAndFabVisible by remember { mutableStateOf(true) }
    var previousFirstVisibleItemIndex by remember { mutableIntStateOf(0) }
    var previousFirstVisibleItemScrollOffset by remember { mutableIntStateOf(0) }

    LaunchedEffect(globalLazyListState) {
        snapshotFlow { 
            globalLazyListState.firstVisibleItemIndex to globalLazyListState.firstVisibleItemScrollOffset 
        }.collectLatest { (index, offset) ->
            if (globalLazyListState.isScrollInProgress) {
                if (index > previousFirstVisibleItemIndex) {
                    isBottomBarAndFabVisible = false
                } else if (index < previousFirstVisibleItemIndex) {
                    isBottomBarAndFabVisible = true
                } else {
                    if (offset > previousFirstVisibleItemScrollOffset) {
                        isBottomBarAndFabVisible = false
                    } else if (offset < previousFirstVisibleItemScrollOffset) {
                        isBottomBarAndFabVisible = true
                    }
                }
            }
            previousFirstVisibleItemIndex = index
            previousFirstVisibleItemScrollOffset = offset
        }
    }

    val filteredList by remember(subjectsList, searchSubject) {
        derivedStateOf {
            if (searchSubject.isEmpty()) {
                subjectsList
            } else {
                subjectsList.filter { it.subject.contains(searchSubject, ignoreCase = true) }
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            isAiLoading = true
            scope.launch {
                try {
                    val base64 = withContext(Dispatchers.IO) {
                        val stream = context.contentResolver.openInputStream(it)
                        encodeImageToBase64(stream!!)
                    }
                    sendToGemini(
                        apiKey = "AIzaSyC4fOZLV3qaIWmNjfM5HotQXYFN6g4qzAE",
                        base64,
                        viewModel
                    ) { success ->
                        isAiLoading = false
                        if (success) {
                            Log.d("AI", "Timetable extracted successfully")
                        } else {
                            Log.e("AI", "Failed to extract timetable")
                        }
                    }
                } catch (e: Exception) {
                    isAiLoading = false
                    e.printStackTrace()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(
                visible = isBottomBarAndFabVisible,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        scheduleToEdit = null
                        showAddSheet = true
                    },
                    containerColor = Color(0xFF262626),
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 20.dp), // Removed padding(paddingValues) to fix nested scaffold top padding
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Subjects",
                    color = Color(0xFF1A1A1A),
                    fontFamily = FontFamily(Font(R.font.plusjakartasansbold)),
                    fontSize = 30.sp,
                )
                Box(
                    contentAlignment = Alignment.TopEnd
                ) {
                    DropdownMenuExample(
                        vIewModel = viewModel,
                        isEditMode = isEditMode,
                        onEditModeToggle = { isEditMode = !isEditMode }
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Spring Semester 2025",
                color = Color(0xFF6B7280),
                fontFamily = FontFamily(Font(R.font.plusjakartasansregular)),
                fontSize = 18.sp,
            )
            Spacer(modifier = Modifier.height(20.dp))

            SBar(searchSubject, placeholder = "Search subjects...", onQueryChange = { searchSubject = it })

            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(15.dp),
                state = globalLazyListState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredList) { sub ->
                    SubjectCard(
                        subname = sub.subject,
                        teacher = sub.teacher,
                        daysSchedule = sub.scheduleday,
                        color = Color(sub.color.toULong()),
                        isEditMode = isEditMode,
                        onClick = {
                            if (!isEditMode) {
                                selectedSubjectForHistory = sub
                                showHistoryDialog = true
                            }
                        },
                        onEditClick = {
                            scheduleToEdit = sub
                            showAddSheet = true
                        },
                        onDeleteClick = {
                            viewModel.deleteSchedule(sub)
                        }
                    )
                }
                item { Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding() + 80.dp)) }
            }
        }
    }

    if (isAiLoading) {
        LottieLoader("AI is processing your timetable...", R.raw.handloader)
    }

    if (showAddSheet) {
        ModalBottomSheet(
            onDismissRequest = { 
                showAddSheet = false
                scheduleToEdit = null
            },
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                AddClassScreen(
                    viewModel = viewModel, 
                    scheduleToEdit = scheduleToEdit,
                    onDimiss = { 
                        showAddSheet = false
                        scheduleToEdit = null
                    }
                )
            }
        }
    }

    if (showHistoryDialog && selectedSubjectForHistory != null) {
        AnimatedDialog(showDialog = showHistoryDialog, onDismiss = { showHistoryDialog = false }) {
            AttendanceHistoryDialog(
                viewModel = viewModel,
                subject = selectedSubjectForHistory!!,
                onDismiss = { showHistoryDialog = false }
            )
        }
    }
}

@Composable
fun AttendanceHistoryDialog(
    viewModel: MainVIewModel,
    subject: Schedule,
    onDismiss: () -> Unit
) {
    val history by viewModel.getAttendanceHistory(subject.subjectId).observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Attendance History",
            fontFamily = FontFamily(Font(R.font.plusjakartasansbold)),
            fontSize = 20.sp,
            color = Color.Black
        )
        Text(
            text = subject.subject,
            fontFamily = plusJak,
            fontSize = 16.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (history.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No attendance marked yet.",
                    fontFamily = plusJak,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.height(300.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(history) { record ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF5F5F5))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = record.date,
                                fontFamily = plusJak,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                            Text(
                                text = record.day,
                                fontFamily = plusJak,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (record.attendanceStatus == 1) Color(0xFFE8F5E9) 
                                    else Color(0xFFFFEBEE)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (record.attendanceStatus == 1) "Present" else "Absent",
                                color = if (record.attendanceStatus == 1) Color(0xFF2E7D32) 
                                        else Color(0xFFC62828),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Close",
            modifier = Modifier
                .align(Alignment.End)
                .clickable { onDismiss() }
                .padding(8.dp),
            fontFamily = plusJak,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF262626)
        )
    }
}

@Composable
fun SubjectCard(
    subname: String,
    teacher: String,
    color: Color,
    daysSchedule: List<DaySchedule>,
    isEditMode: Boolean = false,
    onClick: () -> Unit,
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(color)
                .clickable { onClick() }
                .padding(horizontal = 18.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = subname,
                        fontFamily = FontFamily(Font(R.font.plusjakartasansmedium)),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = teacher,
                        fontFamily = plusJak,
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )
                }
                
                if (isEditMode) {
                    Row {
                        IconButton(onClick = onEditClick) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color(0xFF262626),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        IconButton(onClick = onDeleteClick) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.Red,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            daysSchedule.forEach { schedule ->
                Text(
                    text = "${schedule.day}\t\t\t${schedule.timing}",
                    fontFamily = plusJak,
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview
@Composable
fun SubjectCardPreview() {
    SubjectCard(
        subname = "Mathematics",
        teacher = "Mr. Smith",
        color = Color(0xF8DFECDE),
        daysSchedule = listOf(DaySchedule("Mon", "11:00-12:00")),
        onClick = {}
    )
}

@Composable
fun Daylist(
    selectedDay: String,
    onDaySelected: (String) -> Unit
) {
    val days = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
                .height(1.dp)
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items(days) { item ->
                val isSelected = selectedDay == item
                Text(
                    text = item,
                    fontFamily = plusJak,
                    fontSize = 13.5.sp,
                    modifier = Modifier
                        .background(if (isSelected) Color.Black else Color.Transparent)
                        .clickable { onDaySelected(item) }
                        .padding(horizontal = 14.dp, vertical = 14.dp),
                    color = if (isSelected) Color.White else Color.Black
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
                .height(1.dp)
        )
    }
}

@Composable
fun AddTimeSlot() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Black)
            .padding(vertical = 14.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Add Time Slot",
            fontSize = 16.sp,
            fontFamily = plusJak,
            color = Color.White
        )
    }
}

@Composable
fun UpperBox() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 5.dp)
    ) {
        Text(
            text = "Class\nSchedule",
            fontFamily = FontFamily(Font(R.font.plusjakartasansbold)),
            color = Color(0xFF262626),
            fontSize = 35.sp
        )
        Text(
            text = "\n\nSave",
            fontFamily = plusJak,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )
    }
}
