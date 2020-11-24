package com.rainbow.remote.source

import com.rainbow.remote.Listing
import com.rainbow.remote.RedditResponse
import com.rainbow.remote.dto.RemoteMod
import com.rainbow.remote.dto.RemoteRule
import com.rainbow.remote.dto.RemoteSubreddit

interface RemoteSubredditDataSource {

    suspend fun getSubredditAbout(subredditName: String): RedditResponse<RemoteSubreddit>

    suspend fun getSubredditMods(subredditName: String): RedditResponse<Listing<RemoteMod>>

    suspend fun getSubredditRules(subredditName: String): RedditResponse<List<RemoteRule>>

    suspend fun subscribe(subredditId: String)

    suspend fun unSubscribe(subredditId: String)

}