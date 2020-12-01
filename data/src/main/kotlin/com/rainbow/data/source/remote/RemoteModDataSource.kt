package com.rainbow.data.source.remote

import com.rainbow.remote.RedditResponse
import com.rainbow.remote.UserListing
import com.rainbow.remote.client
import com.rainbow.remote.dto.RemoteMod
import com.rainbow.remote.redditGet
import com.rainbow.remote.source.RemoteModDataSource
import io.ktor.client.*

internal fun RemoteModDataSource(): RemoteModDataSource = RemoteModDataSourceImpl(client)

private class RemoteModDataSourceImpl(private val client: HttpClient) : RemoteModDataSource {

    override suspend fun getSubredditMods(subredditName: String): RedditResponse<UserListing<RemoteMod>> {
        val url = "r/$subredditName/about/moderators"
        return client.redditGet(url)
    }

}