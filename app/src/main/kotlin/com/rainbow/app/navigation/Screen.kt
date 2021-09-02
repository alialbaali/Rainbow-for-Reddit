package com.rainbow.app.navigation

sealed class Screen {

    data class Subreddit(val subredditName: String) : Screen()

    data class User(val userName: String) : Screen()

    object Profile : Screen()

}