package com.rainbow.desktop.navigation

import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Sidebar(
    navigationItem: Screen.NavigationItem,
    onNavigationItemClick: (Screen.NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationRail(modifier) {
        Screen.NavigationItem.All.forEach { item ->
            NavigationRailItem(
                selected = item == navigationItem,
                onClick = { onNavigationItemClick(item) },
                icon = { Icon(item.icon, item.icon.name) },
                label = { Text(item.title) },
                alwaysShowLabel = false
            )
        }
    }
}