package com.rainbow.data.source.remote

import com.rainbow.remote.RedditResponse
import com.rainbow.remote.client
import com.rainbow.remote.customRedditRequest
import com.rainbow.remote.dto.RemoteRule
import com.rainbow.remote.source.RemoteRuleDataSource
import io.ktor.client.*

internal fun RemoteRuleDataSource(): RemoteRuleDataSource = RemoteRuleDataSourceImpl(client)

private class RemoteRuleDataSourceImpl(private val client: HttpClient) : RemoteRuleDataSource {

    @Suppress("UNCHECKED_CAST")
    override suspend fun getSubredditRules(subredditName: String): RedditResponse<List<RemoteRule>> {
        val url = "r/$subredditName/about/rules"
        val response = client.customRedditRequest<Map<String, Any>>(url)
            ?.getOrDefault("rules", emptyList<List<RemoteRule>>()) as List<RemoteRule>?
        return when (response) {
            null -> RedditResponse.Failure("Error", 400)
            else -> RedditResponse.Success(data = response)
        }
    }

}