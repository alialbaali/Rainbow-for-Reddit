package com.rainbow.remote.source

import com.rainbow.remote.dto.RemoteItem

interface RemoteItemDataSource {

    suspend fun getUserOverviewItems(
        userName: String,
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): Result<List<RemoteItem>>

    suspend fun getUserSavedItems(
        userName: String,
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): Result<List<RemoteItem>>

}