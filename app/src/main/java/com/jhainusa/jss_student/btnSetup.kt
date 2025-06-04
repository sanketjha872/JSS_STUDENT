package com.jhainusa.jss_student


import androidx.annotation.DrawableRes
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun btbar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Exams,
        BottomNavItem.Graph,
        BottomNavItem.Setting
    )



    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.White,
        elevation = 5.dp
    ) {
        val currentroute = navController.currentBackStackEntryAsState()
            .value?.destination?.route
        items.forEach { item ->
            val selected = currentroute == item.route

            val size by animateDpAsState(
                targetValue = if(selected) 47.5.dp else 28.5.dp,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
            BottomNavigationItem(
                modifier = Modifier.navigationBarsPadding(),

                icon = {
                    Box(
                        modifier = Modifier.size(size)
                            .clip(CircleShape)
                            .background(if(selected) Color.Black else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(painter = painterResource(item.icon), contentDescription = null,
                            tint = if(selected) Color.White else Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }},
                label = null,
                selected = selected,
                onClick = {
                    if (currentroute != item.route) {
                        navController.navigate(item.route)
                        {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}

sealed class BottomNavItem(
    val route : String,
   @DrawableRes val icon : Int
){
    object Home :BottomNavItem("home", R.drawable.icons8_home_48)
    object Graph :BottomNavItem("Graph", R.drawable.chart_square_svgrepo_com)
    object Exams :BottomNavItem("Exams", R.drawable.calendar_svgrepo_com)
    object Setting :BottomNavItem("Setting", R.drawable.setting_2_svgrepo_com)

}