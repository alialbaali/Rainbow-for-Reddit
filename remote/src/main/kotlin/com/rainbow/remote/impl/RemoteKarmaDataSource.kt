package com.rainbow.remote.impl

import com.rainbow.remote.client.redditClient
import com.rainbow.remote.dto.RemoteKarma
import com.rainbow.remote.getOrThrow
import com.rainbow.remote.source.RemoteKarmaDataSource
import io.ktor.client.*

class RemoteKarmaDataSourceImpl(private val client: HttpClient = redditClient) : RemoteKarmaDataSource {

    override suspend fun getProfileKarma(): List<RemoteKarma> {
        return client.getOrThrow(Endpoint.Karma.GetMine)
    }

}