package com.jhainusa.jss_student

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.jhainusa.jss_student.RoomDatabase.MainVIewModel
import com.jhainusa.jss_student.RoomDatabase.MainViewModelFactory
import com.jhainusa.jss_student.RoomDatabase.ScheduleDatabase
import com.jhainusa.jss_student.RoomDatabase.ScheduleRepository
import com.jhainusa.jss_student.UserPref.NameViewModel
import com.jhainusa.jss_student.ciaPaperPage.Papers
import com.jhainusa.jss_student.UserPref.UserInfoScreen
import com.jhainusa.jss_student.ciaPaperPage.InternalsListScreen
import com.jhainusa.jss_student.ciaPaperPage.PaperListScreen
import com.jhainusa.jss_student.ciaPaperPage.Routes
import com.jhainusa.jss_student.ciaPaperPage.SemesterListScreen

class MainActivity : ComponentActivity() {
    lateinit var viewModel: MainVIewModel

    @OptIn(ExperimentalAnimationApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = ScheduleDatabase.getDatabase(applicationContext)
        val dao = database.ScheduleDao()
        val classDao = database.classScheduleDao()
        val repository = ScheduleRepository(dao, classDao)
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(repository)
        ).get(MainVIewModel::class.java)

        enableEdgeToEdge()
        setContent {
            val navController = rememberAnimatedNavController()
            AnimatedNavHost(navController, startDestination = "userinfo",
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300)
                    )
                }){
                composable("userinfo"){
                    UserInfoScreen(navController = navController)
                }
                composable("AllScreenNav"){
                    AllScreenNav(viewModel,navController)
                }

                composable(
                    route = "${Routes.SEMESTER_LIST}/{yearId}",
                    arguments = listOf(navArgument("yearId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val yearId = backStackEntry.arguments?.getString("yearId") ?: return@composable
                    SemesterListScreen(
                        yearId = yearId,
                        onSemesterSelected = { semId ->
                            navController.navigate("${Routes.PAPER_LIST}/$yearId/$semId")
                        }
                    )
                }
                composable(
                    route = "${Routes.PAPER_LIST}/{yearId}/{semesterId}",
                    arguments = listOf(navArgument("yearId") { type = NavType.StringType },
                            navArgument("semesterId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val yearId = backStackEntry.arguments?.getString("yearId") ?: return@composable
                    val semesterId = backStackEntry.arguments?.getString("semesterId") ?: return@composable
                    InternalsListScreen(
                        yearId = yearId,
                        semId = semesterId,
                        onInternalSelected = { paperId ->
                            navController.navigate("${Routes.PDF_LIST}/$yearId/$semesterId/$paperId")
                        }
                    )
                }

                composable(
                    route = "${Routes.PDF_LIST}/{yearId}/{semesterId}/{paperId}",
                    arguments = listOf(
                        navArgument("yearId") { type = NavType.StringType },
                        navArgument("semesterId") { type = NavType.StringType },
                        navArgument("paperId") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val yearId = backStackEntry.arguments?.getString("yearId") ?: return@composable
                    val semesterId = backStackEntry.arguments?.getString("semesterId") ?: return@composable
                    val paperId = backStackEntry.arguments?.getString("paperId") ?: return@composable
                    PaperListScreen(yearId,semesterId,paperId)
                }
            }
        }
    }
}
@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AllScreenNav(viewModel: MainVIewModel,mainNav: NavController){
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { btbar(navController) },
        containerColor = Color.White
    ) { innerPadding ->
        AnimatedNavHost(
            navController = navController ,
            startDestination = BottomNavItem.Home.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            modifier = Modifier
                .padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route
            ) { FullPAge(viewModel) }
            composable(BottomNavItem.Graph.route){ TimeTable(viewModel) }
            composable(BottomNavItem.Exams.route){ Papers(mainNav) }
            composable(BottomNavItem.Setting.route,
                ) { UploadTimeTableScreen(viewModel)}
        }
    }
}
