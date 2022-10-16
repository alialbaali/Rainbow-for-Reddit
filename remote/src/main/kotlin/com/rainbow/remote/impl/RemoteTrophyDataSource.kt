package com.rainbow.remote.impl

import com.rainbow.remote.Item
import com.rainbow.remote.client.redditClient
import com.rainbow.remote.dto.RemoteTrophy
import com.rainbow.remote.getOrThrow
import com.rainbow.remote.source.RemoteTrophyDataSource
import io.ktor.client.*
import kotlinx.serialization.Serializable

class RemoteTrophyDataSourceImpl(private val client: HttpClient = redditClient) : RemoteTrophyDataSource {

    override suspend fun getUserTrophies(userName: String): List<RemoteTrophy> {
        return client.getOrThrow<TrophyResponse>(Endpoint.Trophies.User(userName)).trophies.map { it.data }
    }

}

@Serializable
private data class TrophyResponse(val trophies: List<Item<RemoteTrophy>> = emptyList())