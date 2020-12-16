package com.rainbow.data.source.remote

import com.rainbow.remote.*
import com.rainbow.remote.Endpoint.*
import com.rainbow.remote.dto.RemoteRule
import com.rainbow.remote.source.RemoteRuleDataSource
import io.ktor.client.*

internal fun RemoteRuleDataSource(client: HttpClient): RemoteRuleDataSource = RemoteRuleDataSourceImpl(client)

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