package com.rainbow.data.source.remote

import com.rainbow.remote.*
import com.rainbow.remote.dto.RemoteMod
import com.rainbow.remote.dto.RemoteRule
import com.rainbow.remote.dto.RemoteSubreddit
import com.rainbow.remote.source.RemoteSubredditDataSource
import io.ktor.client.*
import io.ktor.client.request.*

internal fun RemoteSubredditDataSource(): RemoteSubredditDataSource = RemoteSubredditDataSourceImpl(client)

private class RemoteSubredditDataSourceImpl(private val client: HttpClient) : RemoteSubredditDataSource {

    override suspend fun getSubredditAbout(subredditName: String): RedditResponse<RemoteSubreddit> {
        val url = "r/$subredditName/about"
        return client.redditGet(url)
    }

    override suspend fun getSubredditMods(subredditName: String): RedditResponse<Listing<RemoteMod>> {
        val url = "r/$subredditName/about/moderators"
        return client.redditGet(url)
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun getSubredditRules(subredditName: String): RedditResponse<List<RemoteRule>> {
        val url = "r/$subredditName/about/rules"
        return client.redditGet<Map<String, Any>>(url)
            .map { it["rules"] as List<RemoteRule> }
    }

    override suspend fun subscribe(subredditId: String) {
        val url = "api/subscribe"
        return client.redditSubmitForm(url) {
            parameter("sr", subredditId)
            parameter("action", "sub")
        }
    }

    override suspend fun unSubscribe(subredditId: String) {
        val url = "api/subscribe"
        return client.redditSubmitForm(url) {
            parameter("sr", subredditId)
            parameter("action", "unsub")
        }
    }

}