package com.rainbow.remote.impl

import com.rainbow.remote.FlairSelectorResponse
import com.rainbow.remote.client.redditClient
import com.rainbow.remote.dto.CurrentFlair
import com.rainbow.remote.dto.RemoteFlair
import com.rainbow.remote.impl.Endpoint.Flairs
import com.rainbow.remote.requestOrThrow
import com.rainbow.remote.source.RemoteSubredditFlairDataSource
import com.rainbow.remote.submitFormOrThrow
import io.ktor.client.*
import io.ktor.client.request.*

class RemoteSubredditFlairDataSourceImpl(
    private val client: HttpClient = redditClient
) : RemoteSubredditFlairDataSource {

    override suspend fun getCurrentSubredditFlair(subredditName: String): RemoteFlair {
        return client.submitFormOrThrow<FlairSelectorResponse>(Flairs.GetCurrentSubredditFlair(subredditName))
            .current.toFlair()
    }

    override suspend fun getSubredditFlairs(subredditName: String): List<RemoteFlair> {
        return client.requestOrThrow(Flairs.GetSubredditFlairs(subredditName))
    }

    override suspend fun selectSubredditFlair(subredditName: String, userName: String, flairId: String) {
        return client.submitFormOrThrow(Flairs.SelectSubredditFlair(subredditName)) {
            parameter(Keys.FlairId, flairId)
            parameter(Keys.Name, userName)
        }
    }

    override suspend fun unSelectSubredditFlair(subredditName: String, userName: String) {
        return client.submitFormOrThrow(Flairs.UnSelectSubredditFlair(subredditName)) {
            parameter(Keys.Name, userName)
        }
    }

    override suspend fun enableSubredditFlair(subredditName: String) {
        return client.submitFormOrThrow(Flairs.EnableSubredditFlair(subredditName)) {
            parameter(Keys.isFlairEnabled, true)
        }
    }

    override suspend fun disableSubredditFlair(subredditName: String) {
        return client.submitFormOrThrow(Flairs.DisableSubredditFlair(subredditName)) {
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