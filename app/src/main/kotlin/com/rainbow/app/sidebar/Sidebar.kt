package com.rainbow.app.sidebar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rainbow.app.sidebar.SidebarItem.Profile
import com.rainbow.app.sidebar.SidebarItem.Settings
import com.rainbow.app.ui.dimensions
import com.rainbow.app.utils.defaultPadding

@Composable
fun Sidebar(
    sidebarItem: SidebarItem,
    isExpanded: Boolean,
    onClick: (SidebarItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    @Composable
    fun SidebarItem.iconColor() =
        if (sidebarItem == this)
            MaterialTheme.colors.primary
        else
            MaterialTheme.colors.onBackground.copy(0.5F)

    @Composable
    fun SidebarItem.textStyle() = MaterialTheme.typography.subtitle2.copy(iconColor())

    @Composable
    fun SidebarItem.background() =
        if (sidebarItem == this)
            MaterialTheme.colors.primary.copy(0.075F)
        else
            MaterialTheme.colors.background

    Column(
        modifier
            .defaultPadding(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        with(Profile) {
            SidebarItem(
                this,
                isExpanded,
                onClick,
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(clipShape)
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

            SidebarItem.values().onEach { item ->
                if (item != Profile && item != Settings)
                    SidebarItem(
                        item,
                        isExpanded,
                        onClick,
                        Modifier
                            .padding(vertical = MaterialTheme.dimensions.medium)
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clip(item.clipShape)
                            .background(item.background()),
                        item.textStyle(),
                        item.iconColor(),
                    )
            }
        }

        with(Settings) {
            SidebarItem(
                this,
                isExpanded,
                onClick,
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(clipShape)
                    .background(background()),
                textStyle(),
                iconColor(),
            )
        }
    }
}

private inline val SidebarItem.clipShape
    @Composable
    get() = MaterialTheme.shapes.large