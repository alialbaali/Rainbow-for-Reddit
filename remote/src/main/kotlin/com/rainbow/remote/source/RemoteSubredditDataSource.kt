package com.rainbow.remote.source

import com.rainbow.remote.dto.RemotePostRequirements
import com.rainbow.remote.dto.RemoteSubreddit

interface RemoteSubredditDataSource {

    suspend fun getSubreddit(subredditName: String): RemoteSubreddit

    suspend fun getProfileSubreddits(limit: Int, after: String?): List<RemoteSubreddit>

    suspend fun subscribeSubreddit(subredditId: String): Result<Unit>

    suspend fun unSubscribeSubreddit(subredditId: String): Result<Unit>

    suspend fun favoriteSubreddit(subredditName: String): Result<Unit>

    suspend fun unFavoriteSubreddit(subredditName: String): Result<Unit>

    suspend fun getSubredditSubmitText(subredditName: String): Result<String>

    suspend fun getSubredditPostRequirements(subredditName: String): Result<RemotePostRequirements>

    suspend fun searchSubreddit(subredditName: String, limit: Int, after: String?): List<RemoteSubreddit>

}