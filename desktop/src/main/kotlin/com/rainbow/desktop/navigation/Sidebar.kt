package com.rainbow.desktop.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.defaultPadding

@Composable
fun Sidebar(
    sidebarItem: MainScreen.SidebarItem,
    onSidebarItemClick: (MainScreen.SidebarItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        Box(Modifier.defaultPadding()) {
            Image(painterResource("icons/Rainbow.svg"), RainbowStrings.Rainbow, Modifier.size(48.dp))
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
                MainScreen.SidebarItem.Profile,
                sidebarItem,
                onSidebarItemClick,
            )
            Column {
                listOf(
                    MainScreen.SidebarItem.Home,
                    MainScreen.SidebarItem.Popular,
                    MainScreen.SidebarItem.All,
                    MainScreen.SidebarItem.Subreddits,
                    MainScreen.SidebarItem.Messages,
                ).forEach { item ->
                    SidebarItem(
                        item = item,
                        currentSidebarItem = sidebarItem,
                        onSidebarItemClick,
                    )
                }
            }
            SidebarItem(
                MainScreen.SidebarItem.Settings,
                sidebarItem,
                onSidebarItemClick,
            )
        }
    }
}

@Composable
private fun SidebarItem(
    item: MainScreen.SidebarItem,
    currentSidebarItem: MainScreen.SidebarItem,
    onSidebarItemClick: (MainScreen.SidebarItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationRailItem(
        selected = item == currentSidebarItem,
        onClick = { onSidebarItemClick(item) },
        icon = { Icon(item.icon, item.icon.name) },
        label = { Text(item.title) },
        alwaysShowLabel = false,
        modifier = modifier.clip(MaterialTheme.shapes.small),
        colors = NavigationRailItemDefaults.colors(
            indicatorColor = MaterialTheme.colorScheme.primary,
            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
        )
    )
}