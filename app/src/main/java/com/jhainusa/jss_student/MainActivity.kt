package com.jhainusa.jss_student

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jhainusa.jss_student.RoomDatabase.MainVIewModel
import com.jhainusa.jss_student.RoomDatabase.MainViewModelFactory
import com.jhainusa.jss_student.RoomDatabase.ScheduleDatabase
import com.jhainusa.jss_student.RoomDatabase.ScheduleRepository
import me.saket.swipe.SwipeAction

class MainActivity : ComponentActivity() {
    lateinit var mainVIewModel: MainVIewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = ScheduleDatabase.getDatabase(applicationContext).ScheduleDao()
        val repository = ScheduleRepository(dao)
        mainVIewModel = ViewModelProvider(
            this,
            MainViewModelFactory(repository)
        ).get(MainVIewModel::class.java)


        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            Scaffold(
                bottomBar = { btbar(navController) },
                containerColor = Color.White
            ) { innerPadding ->

                NavHost(
                    navController,
                    startDestination = BottomNavItem.Home.route,
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    composable(BottomNavItem.Home.route) { FullPAge() }
                    composable(BottomNavItem.Graph.route) { TimeTable(mainVIewModel) }
                    composable(BottomNavItem.Exams.route) { Papers() }
                    composable(BottomNavItem.Setting.route) {  }

                }
            }
        }
    }
}
