package com.rainbow.local

import com.rainbow.domain.models.Subreddit
import kotlinx.coroutines.flow.Flow

interface LocalSubredditDataSource {

    val currentUserSubreddits: Flow<List<Subreddit>>

    fun insertSubreddit(subreddit: Subreddit)

    fun subscribeSubreddit(subredditName: String)

    fun unsubscribeSubreddit(subredditName: String)

    fun favoriteSubreddit(subredditName: String)

    fun unFavoriteSubreddit(subredditName: String)

    fun clearSubreddits()

}