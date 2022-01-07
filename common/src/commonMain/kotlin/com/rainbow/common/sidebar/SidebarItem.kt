package com.rainbow.common.sidebar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.rainbow.common.navigation.Screen
import com.rainbow.common.navigation.name
import com.rainbow.common.ui.dpDimensions
import com.rainbow.common.utils.defaultPadding

private inline val Screen.SidebarItem.icon
    @Composable
    get() = when (this) {
        Screen.SidebarItem.Home -> com.rainbow.common.utils.RainbowIcons.Home
        Screen.SidebarItem.Subreddits -> com.rainbow.common.utils.RainbowIcons.GridView
        Screen.SidebarItem.Messages -> com.rainbow.common.utils.RainbowIcons.Message
        Screen.SidebarItem.Profile -> com.rainbow.common.utils.RainbowIcons.Person
        Screen.SidebarItem.Settings -> com.rainbow.common.utils.RainbowIcons.Settings
    }

@Composable
fun SidebarItem(
    sidebarItem: Screen.SidebarItem,
    isExpanded: Boolean,
    onClick: (Screen.SidebarItem) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.h5,
    iconColor: Color = MaterialTheme.colors.onBackground,
) {
    Row(
        modifier
            .clickable { onClick(sidebarItem) }
            .defaultPadding(),
        Arrangement.spacedBy(MaterialTheme.dpDimensions.large),
        Alignment.CenterVertically,
    ) {

        Icon(
            sidebarItem.icon,
            sidebarItem.name,
            Modifier.size(24.dp),
            iconColor
        )

        AnimatedVisibility(isExpanded) {
            Text(
                sidebarItem.name,
                Modifier
                    .fillMaxWidth(),
                style = textStyle
            )
        }
    }
}
