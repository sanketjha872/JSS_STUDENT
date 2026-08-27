package com.jhainusa.jss_student

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.background,
        elevation = 5.dp
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route

            val iconSize by animateDpAsState(
                targetValue = if (selected) 47.5.dp else 28.5.dp,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )


            BottomNavigationItem(
                modifier = Modifier.navigationBarsPadding(),
                icon = {
                    val iconPainter = painterResource(id = item.icon)
                    Box(
                        modifier = Modifier
                            .size(iconSize)
                            .clip(CircleShape)
                            .background(if (selected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = iconPainter,
                            contentDescription = item.route,
                            tint = if (selected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                selected = selected,
                label = null,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

sealed class BottomNavItem(
    val route: String,
    @DrawableRes val icon: Int
) {
    object Home : BottomNavItem("home", R.drawable.icons8_home_48)
    object Graph : BottomNavItem("Graph", R.drawable.calendar_svgrepo_com)
    object Exams : BottomNavItem("Exams", R.drawable.chart_square_svgrepo_com)
    object Setting : BottomNavItem("Setting", R.drawable.setting_2_svgrepo_com)
}
