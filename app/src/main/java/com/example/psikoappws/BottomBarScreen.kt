package com.example.psikoappws

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBarScreen(
        route = "HOME",
        title = "HOME",
        icon = Icons.Default.Home
    )
    object MyQuote : BottomBarScreen(
        route = "QUOTE",
        title = "QUOTE",
        icon = Icons.Default.Star
    )
    object MyDiary : BottomBarScreen(
        route = "DIARY",
        title = "DIARY",
        icon = Icons.Default.Add
    )
    object Setting : BottomBarScreen(
        route = "SETTING",
        title = "SETTING",
        icon = Icons.Default.Settings
    )
}
