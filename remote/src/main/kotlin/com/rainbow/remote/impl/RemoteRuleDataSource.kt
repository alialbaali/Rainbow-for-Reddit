package com.rainbow.remote.impl

import com.rainbow.remote.dto.RemoteRule
import com.rainbow.remote.impl.Endpoint.Rules
import com.rainbow.remote.mainClient
import com.rainbow.remote.plainRequest
import com.rainbow.remote.source.RemoteRuleDataSource
import io.ktor.client.*

fun RemoteRuleDataSource(client: HttpClient = mainClient): RemoteRuleDataSource = RemoteRuleDataSourceImpl(client)

private const val RulesKey = "rules"

private class RemoteRuleDataSourceImpl(private val client: HttpClient) : RemoteRuleDataSource {

    @Suppress("UNCHECKED_CAST")
    override suspend fun getSubredditRules(subredditName: String): Result<List<RemoteRule>> {
        return client.plainRequest<Map<String, Any>>(Rules.Get(subredditName))
            .mapCatching { it.getOrElse(RulesKey) { emptyList<RemoteRule>() } as List<RemoteRule> }

    }

}