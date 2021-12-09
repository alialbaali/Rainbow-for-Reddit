package com.rainbow.remote.impl

import com.rainbow.remote.Listing
import com.rainbow.remote.client.redditClient
import com.rainbow.remote.dto.RemoteItem
import com.rainbow.remote.get
import com.rainbow.remote.source.RemoteItemDataSource
import com.rainbow.remote.toList
import io.ktor.client.*
import io.ktor.client.request.*

class RemoteItemDataSourceImpl(private val client: HttpClient = redditClient) : RemoteItemDataSource {

    override suspend fun getUserOverviewItems(
        userName: String,
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): Result<List<RemoteItem>> = client.get<Listing<RemoteItem>>(Endpoint.Items.OverView(userName, postsSorting)) {
        parameter(Keys.Time, timeSorting)
        parameter(Keys.Limit, limit)
        parameter(Keys.After, after)
    }.map { it.toList() }

    override suspend fun getUserSavedItems(
        userName: String,
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): Result<List<RemoteItem>> = client.get<Listing<RemoteItem>>(Endpoint.Items.Saved(userName, postsSorting)) {
        parameter(Keys.Time, timeSorting)
        parameter(Keys.Limit, limit)
        parameter(Keys.After, after)
    }.map { it.toList() }

}