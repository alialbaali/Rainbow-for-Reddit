package com.rainbow.remote.source

import com.rainbow.remote.dto.RemoteFlair

interface RemoteSubredditFlairDataSource {

    suspend fun getCurrentSubredditFlair(subredditName: String): RemoteFlair

    suspend fun getSubredditFlairs(subredditName: String): List<RemoteFlair>

    suspend fun selectSubredditFlair(subredditName: String, userName: String, flairId: String)

    suspend fun unSelectSubredditFlair(subredditName: String, userName: String)

    suspend fun enableSubredditFlair(subredditName: String): Result<Unit>

    suspend fun disableSubredditFlair(subredditName: String): Result<Unit>

}