package com.rainbow.remote.impl

import com.rainbow.remote.UserListing
import com.rainbow.remote.client.redditClient
import com.rainbow.remote.dto.RemoteModerator
import com.rainbow.remote.get
import com.rainbow.remote.impl.Endpoint.Moderators
import com.rainbow.remote.source.RemoteModeratorDataSource
import io.ktor.client.*

fun RemoteModeratorDataSource(client: HttpClient = redditClient): RemoteModeratorDataSource =
    RemoteModeratorDataSourceImpl(client)

private class RemoteModeratorDataSourceImpl(private val client: HttpClient) : RemoteModeratorDataSource {
    override suspend fun getSubredditModerators(subredditName: String): Result<List<RemoteModerator>> {
        return client.get<UserListing<RemoteModerator>>(Moderators.Get(subredditName))
            .mapCatching { it.children }
    }
}