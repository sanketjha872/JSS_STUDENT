package com.jhainusa.jss_student

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ScrollableScreenWithDisappearingBottomNavAndFab() {
    val items = (1..100).toList()
    val lazyListState = rememberLazyListState()

    // State to track if the FAB and BottomNav should be visible
    var isVisible by remember { mutableStateOf(true) }

    // Derived state to detect scroll direction
    val isScrollingUp by remember {
        derivedStateOf {
            // Check if the first visible item is changing and if the new first visible item
            // is at a lower index than the previous one, or if scrolling within the first item.
            // This logic can be fine-tuned based on the desired sensitivity.
            lazyListState.firstVisibleItemIndex < (lazyListState.layoutInfo.visibleItemsInfo.firstOrNull()?.index
                ?: Int.MAX_VALUE) ||
                    (lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0)
        }
    }

    // Previous first visible item index, to compare with the current one
    var previousFirstVisibleItemIndex by remember { mutableStateOf(lazyListState.firstVisibleItemIndex) }
    var previousFirstVisibleItemScrollOffset by remember { mutableStateOf(lazyListState.firstVisibleItemScrollOffset) }


    LaunchedEffect(
        lazyListState.firstVisibleItemIndex,
        lazyListState.firstVisibleItemScrollOffset
    ) {
        if (lazyListState.firstVisibleItemIndex > previousFirstVisibleItemIndex) {
            // Scrolled Down
            isVisible = false
        } else if (lazyListState.firstVisibleItemIndex < previousFirstVisibleItemIndex) {
            // Scrolled Up
            isVisible = true
        } else {
            // Scrolling within the same item
            if (lazyListState.firstVisibleItemScrollOffset > previousFirstVisibleItemScrollOffset) {
                // Scrolled Down within item
                isVisible = false
            } else if (lazyListState.firstVisibleItemScrollOffset < previousFirstVisibleItemScrollOffset) {
                // Scrolled Up within item
                isVisible = true
            }
        }
        previousFirstVisibleItemIndex = lazyListState.firstVisibleItemIndex
        previousFirstVisibleItemScrollOffset = lazyListState.firstVisibleItemScrollOffset
    }


    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(initialOffsetY = { it }), // Slide in from bottom
                exit = slideOutVertically(targetOffsetY = { it })  // Slide out to bottom
            ) {
                BottomNavigationBar()
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(initialOffsetY = { it / 2 }), // Adjust as needed
                exit = slideOutVertically(targetOffsetY = { it / 2 })
            ) {
                FloatingActionButton(onClick = { /* TODO */ }) {
                    Icon(Icons.Filled.Add, "Add")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { paddingValues ->
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), // Apply padding from Scaffold
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    Text(
                        text = "Item #$item",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                    Divider()
                }
            }
        }
    )
}

@Composable
fun BottomNavigationBar() {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            selected = true,
            onClick = { /* TODO */ },
            label = { Text("Home") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favorites") },
            selected = false,
            onClick = { /* TODO */ },
            label = { Text("Favorites") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
            selected = false,
            onClick = { /* TODO */ },
            label = { Text("Settings") }
        )
    }
}