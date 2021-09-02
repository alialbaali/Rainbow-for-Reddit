package com.rainbow.app

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import com.rainbow.app.message.MessagesScreen
import com.rainbow.app.post.PostContent
import com.rainbow.app.post.PostListType
import com.rainbow.app.profile.ProfileScreen
import com.rainbow.app.settings.SettingsScreen
import com.rainbow.app.sidebar.SidebarItem
import com.rainbow.app.sidebar.SidebarItem.*
import com.rainbow.app.subreddit.Subreddits

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Content(
    item: SidebarItem,
    modifier: Modifier = Modifier,
) {
    when (item) {
        Profile -> ProfileScreen()
        Home -> PostContent(PostListType.Home)
        Subreddits -> Subreddits()
        Messages -> MessagesScreen()
        Settings -> SettingsScreen()
    }
}

@NonRestartableComposable
@Composable
fun <T> PagingEffect(iterable: Iterable<T>, currentIndex: Int, block: (T) -> Unit) {
    SideEffect {
        iterable.onIndex(currentIndex, block)
    }
}

fun <T> Iterable<T>.onIndex(currentIndex: Int, block: (T) -> Unit) {
    withIndex().last().apply {
        if (index == currentIndex)
            block(value)
    }
}