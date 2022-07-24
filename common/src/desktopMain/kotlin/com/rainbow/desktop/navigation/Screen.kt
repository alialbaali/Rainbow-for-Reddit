package com.rainbow.desktop.navigation

import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import com.rainbow.common.utils.RainbowIcons

sealed interface Screen {
    data class Subreddit(val subredditName: String) : Screen
    data class User(val userName: String) : Screen
    data class Search(val searchTerm: String) : Screen
    sealed interface NavigationItem : Screen {
        object Profile : NavigationItem
        object Home : NavigationItem
        object Subreddits : NavigationItem
        object Messages : NavigationItem
        object Settings : NavigationItem

        companion object {
            val All = listOf(Profile, Home, Subreddits, Messages, Settings)
        }
    }
}

inline val Screen.NavigationItem.icon
    @Composable
    get() = when (this) {
        Screen.NavigationItem.Home -> RainbowIcons.Home
        Screen.NavigationItem.Subreddits -> RainbowIcons.GridView
        Screen.NavigationItem.Messages -> RainbowIcons.Message
        Screen.NavigationItem.Profile -> RainbowIcons.Person
        Screen.NavigationItem.Settings -> RainbowIcons.Settings
    }

inline val Screen.NavigationItem.title
    get() = when (this) {
        Screen.NavigationItem.Profile -> "Profile"
        Screen.NavigationItem.Home -> "Home"
        Screen.NavigationItem.Subreddits -> "Subreddits"
        Screen.NavigationItem.Messages -> "Messages"
        Screen.NavigationItem.Settings -> "Settings"
    }