package com.rainbow.remote.impl

import com.rainbow.remote.RedditResponse
import com.rainbow.remote.customRedditRequest
import com.rainbow.remote.dto.RemoteRule
import com.rainbow.remote.impl.Endpoint.Rules
import com.rainbow.remote.mainClient
import com.rainbow.remote.source.RemoteRuleDataSource
import io.ktor.client.*

fun RemoteRuleDataSource(client: HttpClient = mainClient): RemoteRuleDataSource = RemoteRuleDataSourceImpl(client)

private class RemoteRuleDataSourceImpl(private val client: HttpClient) : RemoteRuleDataSource {

    @Suppress("UNCHECKED_CAST")
    override suspend fun getSubredditRules(subredditName: String): RedditResponse<List<RemoteRule>> {
        val path by Rules.Get(subredditName)
        val response = client.customRedditRequest<Map<String, Any>>(path)
            ?.getOrDefault("rules", emptyList<List<RemoteRule>>()) as List<RemoteRule>?
        return when (response) {
            null -> RedditResponse.Failure("Error", 400)
            else -> RedditResponse.Success(data = response)
        }
    }

}