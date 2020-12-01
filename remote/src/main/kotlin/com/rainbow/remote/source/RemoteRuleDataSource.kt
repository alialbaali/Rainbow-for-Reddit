package com.rainbow.remote.source

import com.rainbow.remote.RedditResponse
import com.rainbow.remote.dto.RemoteRule

interface RemoteRuleDataSource {

    suspend fun getSubredditRules(subredditName: String): RedditResponse<List<RemoteRule>>

}