package com.rainbow.remote.impl

import com.rainbow.remote.FlairSelectorResponse
import com.rainbow.remote.dto.CurrentFlair
import com.rainbow.remote.dto.RemoteFlair
import com.rainbow.remote.impl.Endpoint.Flairs
import com.rainbow.remote.mainClient
import com.rainbow.remote.plainRequest
import com.rainbow.remote.source.RemoteFlairDataSource
import com.rainbow.remote.submitForm
import io.ktor.client.*
import io.ktor.client.request.*

fun RemoteFlairDataSource(client: HttpClient = mainClient): RemoteFlairDataSource = RemoteFlairDataSourceImpl(client)

private class RemoteFlairDataSourceImpl(private val client: HttpClient) : RemoteFlairDataSource {

    override suspend fun getCurrentSubredditFlair(subredditName: String): Result<RemoteFlair> {
        return client.submitForm<FlairSelectorResponse>(Flairs.GetCurrentSubredditFlair(subredditName))
            .mapCatching { it.current.toFlair() }
    }

    override suspend fun getSubredditFlairs(subredditName: String): Result<List<RemoteFlair>> {
        return client.plainRequest(Flairs.GetSubredditFlairs(subredditName))
    }

    override suspend fun selectSubredditFlair(subredditName: String, flairId: String): Result<Unit> {
        return client.submitForm(Flairs.SelectSubredditFlair(subredditName)) {
            parameter(Keys.FlairId, flairId)
            parameter(Keys.Name, "LoneWalker20") // UserName
        }
    }

    override suspend fun unSelectSubredditFlair(subredditName: String): Result<Unit> {
        return client.submitForm(Flairs.UnSelectSubredditFlair(subredditName)) {
            parameter(Keys.Name, "LoneWalker20") // UserName
        }
    }

    override suspend fun enableSubredditFlair(subredditName: String): Result<Unit> {
        return client.submitForm(Flairs.EnableSubredditFlair(subredditName)) {
            parameter(Keys.isFlairEnabled, true)
        }
    }

    override suspend fun disableSubredditFlair(subredditName: String): Result<Unit> {
        return client.submitForm(Flairs.DisableSubredditFlair(subredditName)) {
            parameter(Keys.isFlairEnabled, false)
        }
    }

    private fun CurrentFlair.toFlair(): RemoteFlair {
        return RemoteFlair(
            text = flairText,
            cssClass = flairCssClass,
            id = flairTemplateId,
        )
    }
}