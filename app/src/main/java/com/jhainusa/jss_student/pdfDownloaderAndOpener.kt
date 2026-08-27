package com.jhainusa.jss_student

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

@Composable
fun PdfDownloaderAndOpener(pdfUrl: String,papertype : String) {
    val context = LocalContext.current
    var isDownloading by remember { mutableStateOf(false) }

    LaunchedEffect(pdfUrl) {
        if (pdfUrl.isNotEmpty()) {
            isDownloading = true
            downloadAndOpenPdf(context, pdfUrl,papertype)
            isDownloading = false
        }
    }

    if (isDownloading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Downloading PDF...")
        }
    }
}

suspend fun downloadAndOpenPdf(context: Context, url: String, papertype: String) {
    withContext(Dispatchers.IO) {
        try {
            val fileName = "$papertype.pdf"
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            val file = File(storageDir, fileName)

            // Check if file already exists for offline access
            if (file.exists()) {
                withContext(Dispatchers.Main) {
                    openPdfFile(context, file)
                }
                return@withContext
            }

            // Clean the URL string from potential quotes or whitespace
            val cleanedUrl = url.trim().removeSurrounding("\"")

            val urlConnection = URL(cleanedUrl).openConnection()
            val inputStream = BufferedInputStream(urlConnection.getInputStream())
            val outputStream = FileOutputStream(file)

            val dataBuffer = ByteArray(1024)
            var bytesRead: Int

            while (inputStream.read(dataBuffer).also { bytesRead = it } != -1) {
                outputStream.write(dataBuffer, 0, bytesRead)
            }

            inputStream.close()
            outputStream.close()

            withContext(Dispatchers.Main) {
                openPdfFile(context, file)
            }
        } catch (e: Exception) {
            Log.e("PDF", "Download error: ${e.message}")
        }
    }
}

fun openPdfFile(context: Context, file: File) {
    try {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
        }

        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No PDF viewer found", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        Log.e("PDF", "Error opening PDF: ${e.message}")
        Toast.makeText(context, "Error opening PDF", Toast.LENGTH_SHORT).show()
    }
}
