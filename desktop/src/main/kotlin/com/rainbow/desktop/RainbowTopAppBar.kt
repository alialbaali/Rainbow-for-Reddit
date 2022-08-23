package com.rainbow.desktop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.components.RainbowIconButton
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.navigation.title
import com.rainbow.desktop.search.SearchTextField
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.defaultPadding

@Composable
fun RainbowTopAppBar(
    mainScreen: MainScreen,
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
        modifier = modifier.defaultPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(Modifier.weight(1F), horizontalArrangement = Arrangement.SpaceBetween) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RainbowIconButton(
                    RainbowIcons.ArrowBack,
                    RainbowStrings.NavigateBack,
                    onBackClick,
                    enabled = isBackEnabled,
                )

                RainbowIconButton(
                    RainbowIcons.ArrowForward,
                    RainbowStrings.NavigateForward,
                    onForwardClick,
                    enabled = isForwardEnabled,
                )

                RainbowIconButton(
                    RainbowIcons.Refresh,
                    RainbowStrings.Refresh,
                    onRefresh,
                )

                Text(
                    when (mainScreen) {
                        is MainScreen.SidebarItem -> mainScreen.title
                        is MainScreen.Subreddit -> mainScreen.subredditName
                        is MainScreen.User -> mainScreen.userName
                        is MainScreen.Search -> mainScreen.searchTerm
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
            }

        }
        SearchTextField(
            onSearchClick,
            onSubredditNameClick,
            Modifier
                .weight(1F)
                .padding(end = 16.dp)
        )
    }
}