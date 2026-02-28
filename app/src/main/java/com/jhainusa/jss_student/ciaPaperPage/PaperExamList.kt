package com.jhainusa.jss_student.ciaPaperPage

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.util.TableInfo
import com.jhainusa.jss_student.PdfDownloaderAndOpener
import com.jhainusa.jss_student.R
import com.jhainusa.jss_student.downloadAndOpenPdf
import com.jhainusa.jss_student.plusJak

@Composable
fun PaperListScreen(
    yearId: String,
    semId : String,
    papertype : String,
    viewModel: PapersViewModel = viewModel(),
) {
    val papers by viewModel.pdf_Url.collectAsState()

    LaunchedEffect(yearId,semId) {
        viewModel.loadpdfs(yearId,semId,papertype)
    }
    PdfDownloaderAndOpener(papers!!)
    if (!papers.isNullOrEmpty()) {
        PdfDownloaderAndOpener(pdfUrl = papers!!)
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }}
