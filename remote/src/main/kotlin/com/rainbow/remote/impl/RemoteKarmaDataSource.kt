package com.rainbow.remote.impl

import com.rainbow.remote.dto.RemoteKarma
import com.rainbow.remote.get
import com.rainbow.remote.mainClient
import com.rainbow.remote.source.RemoteKarmaDataSource
import io.ktor.client.*

fun RemoteKarmaDataSource(client: HttpClient = mainClient): RemoteKarmaDataSource = RemoteKarmaDataSourceImpl(client)

private class RemoteKarmaDataSourceImpl(private val client: HttpClient) : RemoteKarmaDataSource {

    override suspend fun getMyKarma(): Result<List<RemoteKarma>> {
        return client.get<List<RemoteKarma>>(Endpoint.Karma.GetMine)
    }

}