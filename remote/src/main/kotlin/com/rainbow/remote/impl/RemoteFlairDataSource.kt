package com.rainbow.remote.impl

import com.rainbow.remote.FlairSelectorResponse
import com.rainbow.remote.client.redditClient
import com.rainbow.remote.dto.CurrentFlair
import com.rainbow.remote.dto.RemoteFlair
import com.rainbow.remote.impl.Endpoint.Flairs
import com.rainbow.remote.plainRequest
import com.rainbow.remote.source.RemoteSubredditFlairDataSource
import com.rainbow.remote.submitForm
import io.ktor.client.*
import io.ktor.client.request.*

class RemoteSubredditFlairDataSourceImpl(
    private val client: HttpClient = redditClient
) : RemoteSubredditFlairDataSource {

    override suspend fun getCurrentSubredditFlair(subredditName: String): Result<RemoteFlair> {
        return client.submitForm<FlairSelectorResponse>(Flairs.GetCurrentSubredditFlair(subredditName))
            .mapCatching { it.current.toFlair() }
    }

    override suspend fun getSubredditFlairs(subredditName: String): Result<List<RemoteFlair>> {
        return client.plainRequest(Flairs.GetSubredditFlairs(subredditName))
    }

    override suspend fun selectSubredditFlair(subredditName: String, userName: String, flairId: String): Result<Unit> {
        return client.submitForm(Flairs.SelectSubredditFlair(subredditName)) {
            parameter(Keys.FlairId, flairId)
            parameter(Keys.Name, userName)
        }
    }

    override suspend fun unSelectSubredditFlair(subredditName: String, userName: String): Result<Unit> {
        return client.submitForm(Flairs.UnSelectSubredditFlair(subredditName)) {
            parameter(Keys.Name, userName)
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