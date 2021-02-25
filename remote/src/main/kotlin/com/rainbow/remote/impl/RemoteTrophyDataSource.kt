package com.rainbow.remote.impl

import com.rainbow.remote.Item
import com.rainbow.remote.dto.RemoteTrophy
import com.rainbow.remote.get
import com.rainbow.remote.mainClient
import com.rainbow.remote.source.RemoteTrophyDataSource
import io.ktor.client.*
import kotlinx.serialization.Serializable

fun RemoteTrophyDataSource(client: HttpClient = mainClient): RemoteTrophyDataSource = RemoteTrophyDataSourceImpl(client)

private class RemoteTrophyDataSourceImpl(private val client: HttpClient) : RemoteTrophyDataSource {

    override suspend fun getUserTrophies(userName: String): Result<List<RemoteTrophy>> {
        return client.get<TrophyResponse>(Endpoint.Trophies.User(userName))
            .mapCatching { it.trophies.map { it.data } }
    }

}

@Serializable
private data class TrophyResponse(val trophies: List<Item<RemoteTrophy>> = emptyList())