package com.rainbow.app.subreddit

import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rainbow.app.utils.defaultPadding

enum class SubredditTab {
    Posts, Rules, Resources,
    RelatedSubreddits, Moderators;

    companion object {
        val Default = Posts
    }
}

@Composable
fun SubredditTabRow(
    selectedTab: SubredditTab,
    onTabClick: (SubredditTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    TabRow(
        selectedTab.ordinal,
        modifier,
    ) {
        SubredditTab.values()
            .forEach { tab ->
                SubredditTab(
                    selected = selectedTab == tab,
                    onTabClick = onTabClick,
                    subredditTab = tab,
                )
            }
    }
}

@Composable
private fun SubredditTab(
    selected: Boolean,
    onTabClick: (SubredditTab) -> Unit,
    subredditTab: SubredditTab,
    modifier: Modifier = Modifier,
) {
    Tab(
        selected = selected,
        onClick = { onTabClick(subredditTab) },
        modifier
            .defaultPadding(),
    ) {
        Text(subredditTab.name)
    }
}