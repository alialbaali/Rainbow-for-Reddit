package com.rainbow.app

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rainbow.app.components.RainbowTopAppBar
import com.rainbow.app.sidebar.SidebarState
import com.rainbow.app.subreddit.SubredditScreen
import com.rainbow.app.user.UserScreen

@Composable
fun Content(
    sidebarState: SidebarState,
    onSidebarClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val title = when (sidebarState) {
        is SidebarState.Profile -> "Profile"
        is SidebarState.Page -> sidebarState.page.toString()
        is SidebarState.Subreddit -> sidebarState.subredditName
    }

    Scaffold(
        topBar = {
            RainbowTopAppBar(title, onSidebarClick)
        },
        modifier = modifier,
    ) {

        when (sidebarState) {
            is SidebarState.Profile -> UserScreen("")
            is SidebarState.Page -> MainPageScreen((sidebarState).page)
            is SidebarState.Subreddit -> SubredditScreen((sidebarState).subredditName)
        }

    }
}