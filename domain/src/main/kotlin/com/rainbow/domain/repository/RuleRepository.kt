package com.rainbow.domain.repository

import com.rainbow.domain.models.Rule

interface RuleRepository {

    suspend fun getSubredditRules(subredditName: String): Result<List<Rule>>

}