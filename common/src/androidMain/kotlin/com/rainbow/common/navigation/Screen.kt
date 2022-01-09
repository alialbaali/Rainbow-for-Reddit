package com.rainbow.common.navigation

import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import com.rainbow.common.utils.RainbowIcons

sealed class Screen(val route: String) {
    object Settings : Screen("Settings")
    object Subreddit : Screen("Subreddit")
    object User : Screen("User")
    object Post : Screen("Post")
    object Message : Screen("Message")
    sealed class NavigationItem(route: String) : Screen(route) {
        object Home : NavigationItem("Home")
        object Subreddits : NavigationItem("Subreddits")
        object Search : NavigationItem("Search")
        object Messages : NavigationItem("Messages")
        object Profile : NavigationItem("Profile")

        companion object {
            val All = listOf(Home, Subreddits, Search, Messages, Profile)
        }
    }
    sealed class Sheet(route: String): Screen(route) {
        object HomePostSorting: Sheet("HomePostSorting")
        object UserPostSorting: Sheet("UserPostSorting")
        object SubredditPostSorting: Sheet("SubredditPostSorting")
        object SearchPostSorting: Sheet("SearchPostSorting")
        object PostCommentSorting: Sheet("PostCommentSorting")
    }
}

inline val Screen.NavigationItem.icon
    @Composable
    get() = when (this) {
        Screen.NavigationItem.Home -> RainbowIcons.Home
        Screen.NavigationItem.Subreddits -> RainbowIcons.GridView
        Screen.NavigationItem.Messages -> RainbowIcons.Message
        Screen.NavigationItem.Profile -> RainbowIcons.Person
        Screen.NavigationItem.Search -> RainbowIcons.Search
    }

inline val Screen.NavigationItem.title
    get() = when (this) {
        Screen.NavigationItem.Profile -> "Profile"
        Screen.NavigationItem.Home -> "Home"
        Screen.NavigationItem.Subreddits -> "Subreddits"
        Screen.NavigationItem.Messages -> "Messages"
        Screen.NavigationItem.Search -> "Search"
    }