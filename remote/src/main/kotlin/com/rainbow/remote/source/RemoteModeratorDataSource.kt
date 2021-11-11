package com.rainbow.remote.source

import com.rainbow.remote.dto.RemoteModerator

interface RemoteModeratorDataSource {

    suspend fun getSubredditModerators(subredditName: String): Result<List<RemoteModerator>>

}