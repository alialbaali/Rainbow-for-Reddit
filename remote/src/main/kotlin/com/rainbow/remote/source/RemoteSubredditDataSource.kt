package com.rainbow.remote.source

import com.rainbow.remote.dto.RemoteSubreddit

interface RemoteSubredditDataSource {

    suspend fun getSubredditAbout(subredditName: String): Result<RemoteSubreddit>

    suspend fun getMySubreddits(): Result<List<RemoteSubreddit>>

    suspend fun subscribeSubreddit(subredditId: String): Result<Unit>

    suspend fun unSubscribeSubreddit(subredditId: String): Result<Unit>

    suspend fun favoriteSubreddit(subredditName: String): Result<Unit>

    suspend fun unFavoriteSubreddit(subredditName: String): Result<Unit>

}