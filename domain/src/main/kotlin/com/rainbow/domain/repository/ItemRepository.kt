package com.rainbow.domain.repository

import com.rainbow.domain.models.Item
import com.rainbow.domain.models.TimeSorting
import com.rainbow.domain.models.UserPostSorting

interface ItemRepository {
    suspend fun getCurrentUserOverviewItems(
        postSorting: UserPostSorting,
        timeSorting: TimeSorting,
        after: String?,
    ): Result<List<Item>>

    suspend fun getCurrentUserSavedItems(
        postSorting: UserPostSorting,
        timeSorting: TimeSorting,
        after: String?,
    ): Result<List<Item>>

    suspend fun getUserOverviewItems(
        userName: String, postSorting: UserPostSorting,
        timeSorting: TimeSorting,
        after: String?,
    ): Result<List<Item>>
}