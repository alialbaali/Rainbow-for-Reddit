package com.rainbow.remote.impl

import com.rainbow.remote.client.redditClient
import com.rainbow.remote.dto.RemoteWikiPage
import com.rainbow.remote.get
import com.rainbow.remote.source.RemoteWikiDataSource
import io.ktor.client.*

class RemoteWikiDataSourceImpl(private val client: HttpClient = redditClient) : RemoteWikiDataSource {
    override suspend fun getWikiIndex(subredditName: String): Result<RemoteWikiPage> {
        return client.get(Endpoint.Wiki.Index(subredditName))
    }

    override suspend fun getWikiPage(subredditName: String, pageName: String): Result<RemoteWikiPage> {
        return client.get(Endpoint.Wiki.Page(subredditName, pageName))
    }
}