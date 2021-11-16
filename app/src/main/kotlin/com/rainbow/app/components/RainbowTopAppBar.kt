package com.rainbow.app.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rainbow.app.navigation.Screen
import com.rainbow.app.navigation.name
import com.rainbow.app.search.SearchTextField
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.defaultPadding

@Composable
fun RainbowTopAppBar(
    screen: Screen,
    onSidebarClick: () -> Unit,
    onBackClick: () -> Unit,
    onForwardClick: () -> Unit,
    isBackEnabled: Boolean,
    isForwardEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .defaultPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onSidebarClick,
            ) {
                Icon(RainbowIcons.Menu, RainbowStrings.ShowMenu)
            }

            IconButton(
                onClick = onBackClick,
                enabled = isBackEnabled
            ) {
                Icon(RainbowIcons.ArrowBack, RainbowStrings.ShowMenu)
            }

            IconButton(
                onClick = onForwardClick,
                enabled = isForwardEnabled
            ) {
                Icon(RainbowIcons.ArrowForward, RainbowStrings.ShowMenu)
            }

            Text(
                when (screen) {
                    is Screen.SidebarItem -> screen.name
                    is Screen.Subreddit -> screen.subredditName
                    is Screen.User -> screen.userName
                },
                style = MaterialTheme.typography.h6
            )
        }
        SearchTextField()
    }
}