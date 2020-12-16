package com.rainbow.remote.impl

import com.rainbow.remote.*
import com.rainbow.remote.dto.RemoteMod
import com.rainbow.remote.impl.Endpoint.*
import com.rainbow.remote.mainClient
import com.rainbow.remote.redditGet
import com.rainbow.remote.source.RemoteModDataSource
import io.ktor.client.*

fun RemoteModDataSource(client: HttpClient = mainClient): RemoteModDataSource = RemoteModDataSourceImpl(client)

private class RemoteModDataSourceImpl(private val client: HttpClient) : RemoteModDataSource {

    override suspend fun getSubredditMods(subredditName: String): RedditResponse<UserListing<RemoteMod>> {
        val path by Mods.Get(subredditName)
        return client.redditGet(path)
    }

}