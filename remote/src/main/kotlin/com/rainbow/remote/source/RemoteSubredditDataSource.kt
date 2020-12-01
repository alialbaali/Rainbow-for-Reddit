package com.rainbow.remote.source

import com.rainbow.remote.RedditResponse
import com.rainbow.remote.dto.RemoteSubreddit

interface RemoteSubredditDataSource {

    suspend fun getSubredditAbout(subredditName: String): RedditResponse<RemoteSubreddit>

    suspend fun getMySubreddits(): RedditResponse<List<RemoteSubreddit>>

    suspend fun subscribeSubreddit(subredditId: String): RedditResponse<Unit>

    suspend fun unSubscribeSubreddit(subredditId: String): RedditResponse<Unit>

    suspend fun favoriteSubreddit(subredditName: String): RedditResponse<Unit>

    suspend fun unFavoriteSubreddit(subredditName: String): RedditResponse<Unit>

}