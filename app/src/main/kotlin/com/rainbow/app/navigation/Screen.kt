package com.rainbow.app.navigation

import com.arkivanov.decompose.statekeeper.Parcelable

sealed class Screen : Parcelable {

    data class Subreddit(val subredditName: String) : Screen()

    data class User(val userName: String) : Screen()

    object Profile : Screen()

}