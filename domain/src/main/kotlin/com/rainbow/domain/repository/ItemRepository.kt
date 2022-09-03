package com.rainbow.domain.repository

import com.rainbow.domain.models.Item
import com.rainbow.domain.models.TimeSorting
import com.rainbow.domain.models.UserPostSorting
import kotlinx.coroutines.flow.Flow

interface ItemRepository {

    val profileOverviewItems: Flow<List<Item>>

    val profileSavedItems: Flow<List<Item>>

    val userOverviewItems: Flow<List<Item>>

    suspend fun getProfileOverviewItems(
        postSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastItemId: String?,
    ): Result<Unit>

    suspend fun getProfileSavedItems(
        postSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastItemId: String?,
    ): Result<Unit>

    suspend fun getUserOverviewItems(
        userName: String, postSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastItemId: String?,
    ): Result<Unit>

}