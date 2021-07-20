package com.rainbow.remote.source

import com.rainbow.remote.dto.RemoteFlair

interface RemoteSubredditFlairDataSource {

    suspend fun getCurrentSubredditFlair(subredditName: String): Result<RemoteFlair>

    suspend fun getSubredditFlairs(subredditName: String): Result<List<RemoteFlair>>

    suspend fun selectSubredditFlair(subredditName: String, flairId: String): Result<Unit>

    suspend fun unSelectSubredditFlair(subredditName: String): Result<Unit>

    suspend fun enableSubredditFlair(subredditName: String): Result<Unit>

    suspend fun disableSubredditFlair(subredditName: String): Result<Unit>

}