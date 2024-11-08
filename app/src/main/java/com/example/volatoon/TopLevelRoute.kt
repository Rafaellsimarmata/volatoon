package com.example.volatoon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class TopLevelRoute(val route: String, val icon: ImageVector, val label: String) {
    object Dashboard : TopLevelRoute("dashboard", Icons.Default.Home, "Home")
    object Trending : TopLevelRoute("trending", Icons.Default.Info, "Trending")
    object Search : TopLevelRoute("search", Icons.Default.Search, "Search")
    object Notifications : TopLevelRoute("notifications", Icons.Default.Notifications, "Notification")
    object Profile : TopLevelRoute("profile", Icons.Default.Person, "Profile")
}