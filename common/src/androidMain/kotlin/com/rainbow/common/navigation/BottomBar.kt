package com.rainbow.common.navigation

import androidx.compose.material.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BottomBar(
    navigationItem: Screen.NavigationItem,
    onClick: (Screen.NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier) {
        Screen.NavigationItem.All.forEach { item ->
            NavigationBarItem(
                selected = item == navigationItem,
                onClick = { onClick(item) },
                icon = { Icon(item.icon, item.title) },
                label = { Text(item.title) },
                alwaysShowLabel = false,
            )
        }
    }
}