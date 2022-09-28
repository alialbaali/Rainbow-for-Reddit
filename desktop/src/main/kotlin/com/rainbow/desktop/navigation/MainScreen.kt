package com.rainbow.desktop.navigation

import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings

sealed interface MainScreen {
    data class Subreddit(val subredditName: String) : MainScreen
    data class User(val userName: String) : MainScreen
    data class Search(val searchTerm: String) : MainScreen
    sealed interface SidebarItem : MainScreen {
        object Profile : SidebarItem
        object Home : SidebarItem
        object Popular : SidebarItem
        object All : SidebarItem
        object Subreddits : SidebarItem
        object Messages : SidebarItem
        object Settings : SidebarItem
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
        MainScreen.SidebarItem.All -> RainbowIcons.Layers
        MainScreen.SidebarItem.Popular -> RainbowIcons.TrendingUp
    }

inline val MainScreen.SidebarItem.title
    get() = when (this) {
        MainScreen.SidebarItem.Profile -> RainbowStrings.Profile
        MainScreen.SidebarItem.Home -> RainbowStrings.Home
        MainScreen.SidebarItem.Subreddits -> RainbowStrings.Subreddits
        MainScreen.SidebarItem.Messages -> RainbowStrings.Messages
        MainScreen.SidebarItem.Settings -> RainbowStrings.Settings
        MainScreen.SidebarItem.All -> RainbowStrings.All
        MainScreen.SidebarItem.Popular -> RainbowStrings.Popular
    }