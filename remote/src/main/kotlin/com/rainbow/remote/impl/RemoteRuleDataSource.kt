package com.rainbow.remote.impl

import com.rainbow.remote.client.redditClient
import com.rainbow.remote.dto.RemoteRule
import com.rainbow.remote.dto.RulesResponse
import com.rainbow.remote.impl.Endpoint.Rules
import com.rainbow.remote.plainRequest
import com.rainbow.remote.source.RemoteRuleDataSource
import io.ktor.client.*

fun RemoteRuleDataSource(client: HttpClient = redditClient): RemoteRuleDataSource = RemoteRuleDataSourceImpl(client)

private class RemoteRuleDataSourceImpl(private val client: HttpClient) : RemoteRuleDataSource {
    @Suppress("UNCHECKED_CAST")
    override suspend fun getSubredditRules(subredditName: String): Result<List<RemoteRule>> {
        return client.plainRequest<RulesResponse>(Rules.Get(subredditName))
            .mapCatching { it.rules ?: emptyList() }
    }
}