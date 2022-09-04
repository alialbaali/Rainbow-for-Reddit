package com.rainbow.local

import com.rainbow.domain.models.Subreddit
import kotlinx.coroutines.flow.Flow

interface LocalSubredditDataSource {

    val subreddits: Flow<List<Subreddit>>

    val profileSubreddits: Flow<List<Subreddit>>

    val searchSubreddits: Flow<List<Subreddit>>

    fun getSubreddit(subredditName: String): Flow<Subreddit?>

    fun insertSubreddit(subreddit: Subreddit)

    fun insertProfileSubreddit(subreddit: Subreddit)

    fun insertSearchSubreddit(subreddit: Subreddit)

    fun subscribeSubreddit(subredditName: String)

    fun unsubscribeSubreddit(subredditName: String)

    fun favoriteSubreddit(subredditName: String)

    fun unFavoriteSubreddit(subredditName: String)

    fun clearSubreddits()

    fun clearProfileSubreddits()

    fun clearSearchSubreddits()

}