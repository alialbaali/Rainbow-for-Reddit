package com.rainbow.remote.impl

import com.rainbow.remote.client.Clients
import com.rainbow.remote.dto.RemoteKarma
import com.rainbow.remote.getOrThrow
import com.rainbow.remote.source.RemoteKarmaDataSource
import io.ktor.client.*

class RemoteKarmaDataSourceImpl(private val client: HttpClient = Clients.Reddit) : RemoteKarmaDataSource {

    override suspend fun getProfileKarma(): List<RemoteKarma> {
        return client.getOrThrow(Endpoint.Karma.GetMine)
    }

}