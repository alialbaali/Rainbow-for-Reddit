package com.rainbow.app.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rainbow.app.navigation.Screen
import com.rainbow.app.navigation.name
import com.rainbow.app.post.PostSorting
import com.rainbow.app.search.SearchTextField
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.defaultSurfaceShape
import com.rainbow.domain.models.*

@Composable
inline fun RainbowTopAppBar(
    screen: Screen,
    sorting: Sorting?,
    timeSorting: TimeSorting?,
    noinline onSearchClick: (String) -> Unit,
    noinline onSubredditNameClick: (String) -> Unit,
    noinline onBackClick: () -> Unit,
    noinline onForwardClick: () -> Unit,
    crossinline setPostSorting: (Sorting) -> Unit,
    crossinline setTimeSorting: (TimeSorting) -> Unit,
    isBackEnabled: Boolean,
    isForwardEnabled: Boolean,
    noinline onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .defaultSurfaceShape(shape = RectangleShape)
            .padding(16.dp),
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
            if (sorting != null && timeSorting != null)
                when (sorting) {
                    is HomePostSorting -> PostSorting(
                        postsSorting = sorting,
                        onSortingUpdate = { setPostSorting(it) },
                        timeSorting = timeSorting,
                        onTimeSortingUpdate = { setTimeSorting(it) },
                    )
                    is SubredditPostSorting -> PostSorting(
                        postsSorting = sorting,
                        onSortingUpdate = { setPostSorting(it) },
                        timeSorting = timeSorting,
                        onTimeSortingUpdate = { setTimeSorting(it) }
                    )
                    is UserPostSorting -> PostSorting(
                        postsSorting = sorting,
                        onSortingUpdate = { setPostSorting(it) },
                        timeSorting = timeSorting,
                        onTimeSortingUpdate = { setTimeSorting(it) }
                    )
                    is SearchPostSorting -> PostSorting(
                        postsSorting = sorting,
                        onSortingUpdate = { setPostSorting(it) },
                        timeSorting = timeSorting,
                        onTimeSortingUpdate = { setTimeSorting(it) }
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