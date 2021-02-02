package com.rainbow.remote.source

import com.rainbow.remote.dto.RemotePostRequirements
import com.rainbow.remote.dto.RemoteSubreddit

interface RemoteSubredditDataSource {

    suspend fun getSubreddit(subredditName: String): Result<RemoteSubreddit>

    suspend fun getMySubreddits(limit: Int, after: String): Result<List<RemoteSubreddit>>

    suspend fun subscribeSubreddit(subredditId: String): Result<Unit>

    suspend fun unSubscribeSubreddit(subredditId: String): Result<Unit>

    suspend fun favoriteSubreddit(subredditName: String): Result<Unit>

    suspend fun unFavoriteSubreddit(subredditName: String): Result<Unit>

    suspend fun getSubredditSubmitText(subredditName: String): Result<String>

    suspend fun getSubredditPostRequirements(subredditName: String): Result<RemotePostRequirements>

    suspend fun searchSubreddit(
        subredditName: String,
        sort: String,
        limit: Int,
        after: String,
    ): Result<List<RemoteSubreddit>>

}