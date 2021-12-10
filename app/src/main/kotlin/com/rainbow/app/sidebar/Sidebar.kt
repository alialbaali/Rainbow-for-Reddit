package com.rainbow.app.sidebar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import com.rainbow.app.navigation.Screen
import com.rainbow.app.settings.SettingsModel
import com.rainbow.app.ui.dpDimensions
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.defaultPadding
import com.rainbow.app.utils.defaultSurfaceShape
import com.rainbow.data.Repos
import kotlinx.coroutines.launch

@Composable
fun Sidebar(
    sidebarItem: Screen.SidebarItem,
    onClick: (Screen.SidebarItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isExpanded by SettingsModel.isSidebarExpanded.collectAsState()
    val scope = rememberCoroutineScope()

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
            MaterialTheme.colors.surface

    Column(
        modifier
            .defaultSurfaceShape(shape = RectangleShape)
            .defaultPadding(),
        horizontalAlignment = if (isExpanded) Alignment.Start else Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = {
                scope.launch {
                    Repos.Settings.setIsSidebarExpanded(!isExpanded)
                }
            },
        ) {
            Icon(RainbowIcons.Menu, RainbowStrings.ShowMenu)
        }

        Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
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
}

private inline val Screen.SidebarItem.itemShape
    @Composable
    get() = MaterialTheme.shapes.large

