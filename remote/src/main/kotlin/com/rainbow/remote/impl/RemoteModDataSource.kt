package com.rainbow.remote.impl

import com.rainbow.remote.UserListing
import com.rainbow.remote.client.redditClient
import com.rainbow.remote.dto.RemoteMod
import com.rainbow.remote.get
import com.rainbow.remote.impl.Endpoint.Mods
import com.rainbow.remote.source.RemoteModDataSource
import io.ktor.client.*

fun RemoteModDataSource(client: HttpClient = redditClient): RemoteModDataSource = RemoteModDataSourceImpl(client)

private class RemoteModDataSourceImpl(private val client: HttpClient) : RemoteModDataSource {

    override suspend fun getSubredditMods(subredditName: String): Result<List<RemoteMod>> {
        return client.get<UserListing<RemoteMod>>(Mods.Get(subredditName))
            .mapCatching { it.children }
    }

}