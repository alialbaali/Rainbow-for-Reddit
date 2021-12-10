package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.data.utils.DefaultLimit
import com.rainbow.data.utils.SettingsKeys
import com.rainbow.data.utils.lowercaseName
import com.rainbow.domain.models.Item
import com.rainbow.domain.models.TimeSorting
import com.rainbow.domain.models.UserPostSorting
import com.rainbow.domain.repository.ItemRepository
import com.rainbow.remote.dto.RemoteItem
import com.rainbow.remote.source.RemoteItemDataSource
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@OptIn(ExperimentalSettingsApi::class)
internal class ItemRepositoryImpl(
    private val remoteItemDataSource: RemoteItemDataSource,
    private val settings: FlowSettings,
    private val dispatcher: CoroutineDispatcher,
    private val mapper: Mapper<RemoteItem, Item>,
) : ItemRepository {

    override suspend fun getCurrentUserOverviewItems(
        postSorting: UserPostSorting,
        timeSorting: TimeSorting,
        after: String?,
    ): Result<List<Item>> = withContext(dispatcher) {
        remoteItemDataSource.getUserOverviewItems(
            settings.getString(SettingsKeys.UserName),
            postSorting.lowercaseName,
            timeSorting.lowercaseName,
            DefaultLimit,
            after
        ).map { it.quickMap(mapper) }
    }

    override suspend fun getCurrentUserSavedItems(
        postSorting: UserPostSorting,
        timeSorting: TimeSorting,
        after: String?,
    ): Result<List<Item>> = withContext(dispatcher) {
        remoteItemDataSource.getUserSavedItems(
            settings.getString(SettingsKeys.UserName),
            postSorting.lowercaseName,
            timeSorting.lowercaseName,
            DefaultLimit,
            after
        ).map { it.quickMap(mapper) }
    }

    override suspend fun getUserOverviewItems(
        userName: String,
        postSorting: UserPostSorting,
        timeSorting: TimeSorting,
        after: String?,
    ): Result<List<Item>> = withContext(dispatcher) {
        remoteItemDataSource.getUserOverviewItems(
            userName,
            postSorting.lowercaseName,
            timeSorting.lowercaseName,
            DefaultLimit,
            after
        ).map { it.quickMap(mapper) }
    }
}