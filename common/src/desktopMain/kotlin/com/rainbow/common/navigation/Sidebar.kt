package com.rainbow.common.navigation

import androidx.compose.material.Icon
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Text
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