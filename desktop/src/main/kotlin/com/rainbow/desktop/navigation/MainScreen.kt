package com.rainbow.desktop.navigation

import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import com.rainbow.desktop.utils.RainbowIcons

sealed interface MainScreen {
    data class Subreddit(val subredditName: String) : MainScreen
    data class User(val userName: String) : MainScreen
    data class Search(val searchTerm: String) : MainScreen
    sealed interface SidebarItem : MainScreen {
        object Profile : SidebarItem
        object Home : SidebarItem
        object Subreddits : SidebarItem
        object Messages : SidebarItem
        object Settings : SidebarItem

        companion object {
            val All = listOf(Profile, Home, Subreddits, Messages, Settings)
        }
    }
}

inline val MainScreen.SidebarItem.icon
    @Composable
    get() = when (this) {
        MainScreen.SidebarItem.Home -> RainbowIcons.Home
        MainScreen.SidebarItem.Subreddits -> RainbowIcons.GridView
        MainScreen.SidebarItem.Messages -> RainbowIcons.Message
        MainScreen.SidebarItem.Profile -> RainbowIcons.Person
        MainScreen.SidebarItem.Settings -> RainbowIcons.Settings
    }

inline val MainScreen.SidebarItem.title
    get() = when (this) {
        MainScreen.SidebarItem.Profile -> "Profile"
        MainScreen.SidebarItem.Home -> "Home"
        MainScreen.SidebarItem.Subreddits -> "Subreddits"
        MainScreen.SidebarItem.Messages -> "Messages"
        MainScreen.SidebarItem.Settings -> "Settings"
    }