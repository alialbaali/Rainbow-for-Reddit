package com.rainbow.desktop.navigation

import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import com.rainbow.desktop.utils.RainbowIcons

sealed interface MainScreen {
    data class Subreddit(val subredditName: String) : MainScreen
    data class User(val userName: String) : MainScreen
    data class Search(val searchTerm: String) : MainScreen
    sealed interface NavigationItem : MainScreen {
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

inline val MainScreen.NavigationItem.icon
    @Composable
    get() = when (this) {
        MainScreen.NavigationItem.Home -> RainbowIcons.Home
        MainScreen.NavigationItem.Subreddits -> RainbowIcons.GridView
        MainScreen.NavigationItem.Messages -> RainbowIcons.Message
        MainScreen.NavigationItem.Profile -> RainbowIcons.Person
        MainScreen.NavigationItem.Settings -> RainbowIcons.Settings
    }

inline val MainScreen.NavigationItem.title
    get() = when (this) {
        MainScreen.NavigationItem.Profile -> "Profile"
        MainScreen.NavigationItem.Home -> "Home"
        MainScreen.NavigationItem.Subreddits -> "Subreddits"
        MainScreen.NavigationItem.Messages -> "Messages"
        MainScreen.NavigationItem.Settings -> "Settings"
    }