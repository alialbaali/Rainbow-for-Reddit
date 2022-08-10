package com.rainbow.desktop.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.defaultPadding

@Composable
fun Sidebar(
    navigationItem: Screen.NavigationItem,
    onNavigationItemClick: (Screen.NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        Box(Modifier.defaultPadding()) {
            Image(painterResource("Icon.svg"), RainbowStrings.Rainbow, Modifier.size(48.dp))
        }

        Column(
            Modifier
                .fillMaxHeight()
                .padding(8.dp)
                .selectableGroup(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            SidebarItem(
                Screen.NavigationItem.Profile,
                navigationItem,
                onNavigationItemClick,
            )
            Column {
                listOf(
                    Screen.NavigationItem.Home,
                    Screen.NavigationItem.Subreddits,
                    Screen.NavigationItem.Messages,
                ).forEach { item ->
                    NavigationRailItem(
                        selected = item == navigationItem,
                        onClick = { onNavigationItemClick(item) },
                        icon = { Icon(item.icon, item.icon.name) },
                        label = { Text(item.title) },
                        alwaysShowLabel = false
                    )
                }
            }
            SidebarItem(
                Screen.NavigationItem.Settings,
                navigationItem,
                onNavigationItemClick,
            )
        }
    }
}

@Composable
private fun SidebarItem(
    item: Screen.NavigationItem,
    currentNavigationItem: Screen.NavigationItem,
    onNavigationItemClick: (Screen.NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationRailItem(
        selected = item == currentNavigationItem,
        onClick = { onNavigationItemClick(item) },
        icon = { Icon(item.icon, item.icon.name) },
        label = { Text(item.title) },
        alwaysShowLabel = false,
        modifier = modifier,
    )
}