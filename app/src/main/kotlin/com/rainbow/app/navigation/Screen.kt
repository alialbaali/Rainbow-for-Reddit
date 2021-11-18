package com.rainbow.app.navigation

import com.arkivanov.essenty.parcelable.Parcelable

sealed interface Screen : Parcelable {
    data class Subreddit(val subredditName: String) : Screen
    data class User(val userName: String) : Screen
    data class Search(val searchTerm: String): Screen
    sealed interface SidebarItem : Screen {
        object Profile : SidebarItem
        object Home : SidebarItem
        object Subreddits : SidebarItem
        object Messages : SidebarItem
        object Settings : SidebarItem

        companion object {
            val All = mutableListOf(Profile, Home, Subreddits, Messages, Settings)
        }
    }
}

val Screen.SidebarItem.name
    get() = when (this) {
        Screen.SidebarItem.Profile -> "Profile"
        Screen.SidebarItem.Home -> "Home"
        Screen.SidebarItem.Subreddits -> "Subreddits"
        Screen.SidebarItem.Messages -> "Messages"
        Screen.SidebarItem.Settings -> "Settings"
    }