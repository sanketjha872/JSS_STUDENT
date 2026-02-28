package com.jhainusa.jss_student.ciaPaperPage

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost

// Routes.kt
object Routes {
    const val SEMESTER_LIST = "semesters"
    const val PAPER_LIST = "papers"

    const val PDF_LIST = "pdfs"
}
