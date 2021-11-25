package com.rainbow.domain.utils

private const val UserName = "u/"
private const val SubredditName = "r/"

internal fun String.asUserDisplayName() = UserName + this

internal fun String.asSubredditDisplayName() = SubredditName + this

internal const val OauthUrl = "https://oauth.reddit.com"

internal const val RedditUrl = "https://www.reddit.com"