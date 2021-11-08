package com.rainbow.app.sidebar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.rainbow.app.navigation.Screen
import com.rainbow.app.ui.dpDimensions
import com.rainbow.app.utils.defaultPadding

@Composable
fun Sidebar(
    sidebarItem: Screen.SidebarItem,
    isExpanded: Boolean,
    onClick: (Screen.SidebarItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    @Composable
    fun Screen.SidebarItem.iconColor() =
        if (sidebarItem == this)
            MaterialTheme.colors.primary
        else
            MaterialTheme.colors.onBackground.copy(0.5F)

    @Composable
    fun Screen.SidebarItem.textStyle() = MaterialTheme.typography.subtitle2.copy(iconColor())

    @Composable
    fun Screen.SidebarItem.background() =
        if (sidebarItem == this)
            MaterialTheme.colors.primary.copy(0.1F)
        else
            MaterialTheme.colors.background

    Column(
        modifier
            .defaultPadding(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        with(Screen.SidebarItem.Profile) {
            SidebarItem(
                this,
                isExpanded,
                onClick,
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(itemShape)
                    .background(background()),
                textStyle(),
                iconColor(),
            )
        }

        Column(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            Screen.SidebarItem.All
                .filter { item -> item !is Screen.SidebarItem.Profile && item !is Screen.SidebarItem.Settings }
                .onEach { item ->
                    SidebarItem(
                        item,
                        isExpanded,
                        onClick,
                        Modifier
                            .padding(vertical = MaterialTheme.dpDimensions.medium)
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clip(item.itemShape)
                            .background(item.background()),
                        item.textStyle(),
                        item.iconColor(),
                    )
                }
        }

        with(Screen.SidebarItem.Settings) {
            SidebarItem(
                this,
                isExpanded,
                onClick,
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(itemShape)
                    .background(background()),
                textStyle(),
                iconColor(),
            )
        }
    }
}

private inline val Screen.SidebarItem.itemShape
    @Composable
    get() = MaterialTheme.shapes.large

