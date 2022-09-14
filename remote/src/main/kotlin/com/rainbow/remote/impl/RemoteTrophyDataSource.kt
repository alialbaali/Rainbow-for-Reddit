package com.rainbow.remote.impl

import com.rainbow.remote.Item
import com.rainbow.remote.client.redditClient
import com.rainbow.remote.dto.RemoteTrophy
import com.rainbow.remote.get
import com.rainbow.remote.source.RemoteTrophyDataSource
import io.ktor.client.*
import kotlinx.serialization.Serializable

class RemoteTrophyDataSourceImpl(private val client: HttpClient = redditClient) : RemoteTrophyDataSource {

    override suspend fun getUserTrophies(userName: String): Result<List<RemoteTrophy>> {
        return client.get<TrophyResponse>(Endpoint.Trophies.User(userName))
            .mapCatching { it.trophies.map { it.data } }
    }

}

@Serializable
private data class TrophyResponse(val trophies: List<Item<RemoteTrophy>> = emptyList())