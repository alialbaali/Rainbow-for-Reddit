package com.rainbow.remote.source

import com.rainbow.remote.dto.RemoteMod

interface RemoteModDataSource {

    suspend fun getSubredditMods(subredditName: String): Result<List<RemoteMod>>

}