package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.domain.models.Rule
import com.rainbow.domain.repository.RuleRepository
import com.rainbow.remote.dto.RemoteRule
import com.rainbow.remote.source.RemoteRuleDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal fun RuleRepository(
    remoteRuleDataSource: RemoteRuleDataSource,
    dispatcher: CoroutineDispatcher,
    mapper: Mapper<RemoteRule, Rule>,
): RuleRepository = RuleRepositoryImpl(remoteRuleDataSource, dispatcher, mapper)

private class RuleRepositoryImpl(
    private val remoteRuleDataSource: RemoteRuleDataSource,
    private val dispatcher: CoroutineDispatcher,
    private val mapper: Mapper<RemoteRule, Rule>,
) : RuleRepository {

    override suspend fun getSubredditRules(subredditName: String): Result<List<Rule>> = withContext(dispatcher) {
        remoteRuleDataSource.getSubredditRules(subredditName)
            .mapCatching { it.quickMap(mapper) }
    }

}