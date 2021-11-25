package com.rainbow.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rainbow.app.navigation.Screen
import com.rainbow.app.navigation.name
import com.rainbow.app.search.SearchTextField
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.defaultShape

@Composable
fun RainbowTopAppBar(
    screen: Screen,
    onSearchClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onForwardClick: () -> Unit,
    isBackEnabled: Boolean,
    isForwardEnabled: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            IconButton(
                onClick = onBackClick,
                enabled = isBackEnabled,
                modifier = Modifier
                    .defaultShape(shape = CircleShape)
            ) {
                Icon(RainbowIcons.ArrowBack, RainbowStrings.ShowMenu)
            }

            IconButton(
                onClick = onForwardClick,
                enabled = isForwardEnabled,
                modifier = Modifier.defaultShape(shape = CircleShape)
            ) {
                Icon(RainbowIcons.ArrowForward, RainbowStrings.ShowMenu)
            }

            IconButton(
                onClick = onRefresh,
                Modifier.defaultShape(shape = CircleShape)
            ) {
                Icon(RainbowIcons.Refresh, RainbowStrings.Refresh)
            }

            Text(
                when (screen) {
                    is Screen.SidebarItem -> screen.name
                    is Screen.Subreddit -> screen.subredditName
                    is Screen.User -> screen.userName
                    is Screen.Search -> screen.searchTerm
                },
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold
            )
        }
        SearchTextField(onSearchClick, onSubredditNameClick, Modifier.padding(end = 16.dp))
    }
}