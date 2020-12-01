package com.rainbow.remote.source

import com.rainbow.remote.RedditResponse
import com.rainbow.remote.UserListing
import com.rainbow.remote.dto.RemoteMod

interface RemoteModDataSource {

    suspend fun getSubredditMods(subredditName: String): RedditResponse<UserListing<RemoteMod>>

}