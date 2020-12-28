package com.rainbow.remote.source

import com.rainbow.remote.dto.RemoteRule

interface RemoteRuleDataSource {

    suspend fun getSubredditRules(subredditName: String): Result<List<RemoteRule>>

}