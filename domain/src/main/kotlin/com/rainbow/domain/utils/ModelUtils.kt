package com.rainbow.domain

private const val UserName = "u/"
private const val SubredditName = "r/"

internal fun String.asUserDisplayName() = UserName + this

internal fun String.asSubredditDisplayName() = SubredditName + this