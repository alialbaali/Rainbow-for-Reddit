package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.data.utils.DefaultLimit
import com.rainbow.data.utils.SettingsKeys
import com.rainbow.data.utils.lowercaseName
import com.rainbow.domain.models.*
import com.rainbow.domain.repository.ItemRepository
import com.rainbow.domain.repository.SubredditRepository
import com.rainbow.domain.repository.UserRepository
import com.rainbow.local.LocalItemDataSource
import com.rainbow.remote.dto.RemoteItem
import com.rainbow.remote.source.RemoteItemDataSource
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

@OptIn(ExperimentalSettingsApi::class)
internal class ItemRepositoryImpl(
    private val userRepository: UserRepository,
    private val subredditRepository: SubredditRepository,
    private val remoteItemDataSource: RemoteItemDataSource,
    private val localItemDataSource: LocalItemDataSource,
    private val settings: FlowSettings,
    private val dispatcher: CoroutineDispatcher,
    private val mapper: Mapper<RemoteItem, Item>,
) : ItemRepository {

    override val profileOverviewItems: Flow<List<Item>> = localItemDataSource.profileOverviewItems
    override val profileSavedItems: Flow<List<Item>> = localItemDataSource.profileSavedItems
    override val userOverviewItems: Flow<List<Item>> = localItemDataSource.userOverviewItems

    override suspend fun getProfileOverviewItems(
        postSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastItemId: String?,
    ): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastItemId == null) localItemDataSource.clearProfileOverviewItems()

            remoteItemDataSource.getUserOverviewItems(
                settings.getString(SettingsKeys.UserName),
                postSorting.lowercaseName,
                timeSorting.lowercaseName,
                DefaultLimit,
                lastItemId
            ).quickMap(mapper).mapWithImageUrls().forEach(localItemDataSource::insertProfileOverviewItem)
        }
    }

    override suspend fun getProfileSavedItems(
        postSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastItemId: String?,
    ): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastItemId == null) localItemDataSource.clearProfileSavedItems()

            remoteItemDataSource.getUserSavedItems(
                settings.getString(SettingsKeys.UserName),
                postSorting.lowercaseName,
                timeSorting.lowercaseName,
                DefaultLimit,
                lastItemId
            ).quickMap(mapper).mapWithImageUrls().forEach(localItemDataSource::insertProfileSavedItem)
        }
    }

    override suspend fun getUserOverviewItems(
        userName: String,
        postSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastItemId: String?,
    ): Result<Unit> = runCatching {
        if (lastItemId == null) localItemDataSource.clearUserOverviewItems()

        withContext(dispatcher) {
            remoteItemDataSource.getUserOverviewItems(
                userName,
                postSorting.lowercaseName,
                timeSorting.lowercaseName,
                DefaultLimit,
                lastItemId
            ).quickMap(mapper).mapWithImageUrls().forEach(localItemDataSource::insertUserOverviewItem)
        }
    }

    private suspend fun List<Item>.mapWithImageUrls() = coroutineScope {
        val result = map { item ->
            val subredditName = when (item) {
                is Comment -> item.subredditName
                is Post -> item.subredditName
            }
            val userName = when (item) {
                is Comment -> item.userName
                is Post -> item.userName
            }
            val subredditImageUrl = async {
                subredditRepository.getSubreddit(subredditName).firstOrNull()?.getOrNull()?.imageUrl
            }
            val userImageUrl = async {
                userRepository.getUser(userName).firstOrNull()?.getOrNull()?.imageUrl
            }
            Triple(
                item.id,
                subredditImageUrl,
                userImageUrl
            )
        }
        map { item ->
            val value = result.first { it.first == item.id }
            val subredditImageUrl = value.second
            val userImageUrl = value.third
            when (item) {
                is Comment -> item.copy(
                    subredditImageUrl = subredditImageUrl.await(),
                    userImageUrl = userImageUrl.await()
                )

                is Post -> item.copy(subredditImageUrl = subredditImageUrl.await(), userImageUrl = userImageUrl.await())
            }
        }
    }
}