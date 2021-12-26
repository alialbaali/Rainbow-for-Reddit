package com.rainbow.data.repository

import com.rainbow.data.utils.SettingsKeys
import com.rainbow.domain.models.*
import com.rainbow.domain.repository.SettingsRepository
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@OptIn(ExperimentalSettingsApi::class)
fun SettingsRepository(
    settings: FlowSettings,
    dispatcher: CoroutineDispatcher,
): SettingsRepository = SettingsRepositoryImpl(settings, dispatcher)

@OptIn(ExperimentalSettingsApi::class)
private class SettingsRepositoryImpl(
    private val settings: FlowSettings,
    private val dispatcher: CoroutineDispatcher,
) : SettingsRepository {

    override val isSidebarExpanded: Flow<Boolean> = settings.getBooleanFlow(SettingsKeys.IsSidebarExpanded)

    override val theme: Flow<Theme> = settings.getStringFlow(SettingsKeys.Theme, Theme.System.name)
        .map { Theme.valueOf(it) }

    override suspend fun setTheme(theme: Theme) = withContext(dispatcher) {
        settings.putString(SettingsKeys.Theme, theme.name)
    }

    override val isFullHeight: Flow<Boolean> = settings.getBooleanFlow(SettingsKeys.IsPostFullHeight)

    override suspend fun setIsPostFullHeight(value: Boolean) = withContext(dispatcher) {
        settings.putBoolean(SettingsKeys.IsPostFullHeight, value)
    }

    override val postLayout: Flow<PostLayout> = settings.getStringFlow(SettingsKeys.PostLayout, PostLayout.Card.name)
        .map { PostLayout.valueOf(it) }

    override val profilePostSorting: Flow<ProfilePostSorting> = settings.getStringFlow(SettingsKeys.ProfilePostSorting)
        .map { ProfilePostSorting.valueOf(it) }

    override val homePostSorting: Flow<HomePostSorting> = settings.getStringFlow(SettingsKeys.HomePostSorting)
        .map { HomePostSorting.valueOf(it) }

    override val subredditPostSorting: Flow<SubredditPostSorting> =
        settings.getStringFlow(SettingsKeys.SubredditPostSorting)
            .map { SubredditPostSorting.valueOf(it) }

    override val searchPostSorting: Flow<SearchPostSorting> = settings.getStringFlow(SettingsKeys.SearchPostSorting)
        .map { SearchPostSorting.valueOf(it) }

    override val userPostSorting: Flow<UserPostSorting> = settings.getStringFlow(SettingsKeys.UserPostSorting)
        .map { UserPostSorting.valueOf(it) }

    override suspend fun setPostLayout(value: PostLayout) = withContext(dispatcher) {
        settings.putString(SettingsKeys.PostLayout, value.name)
    }

    override suspend fun setIsSidebarExpanded(value: Boolean) = withContext(dispatcher) {
        settings.putBoolean(SettingsKeys.IsSidebarExpanded, value)
    }

    override suspend fun setHomePostSorting(value: HomePostSorting) = withContext(dispatcher) {
        settings.putString(SettingsKeys.HomePostSorting, value.name)
    }

    override suspend fun setUserPostSorting(value: UserPostSorting) = withContext(dispatcher) {
        settings.putString(SettingsKeys.UserPostSorting, value.name)
    }

    override suspend fun setProfilePostSorting(value: ProfilePostSorting) = withContext(dispatcher) {
        settings.putString(SettingsKeys.ProfilePostSorting, value.name)
    }

    override suspend fun setSubredditPostSorting(value: SubredditPostSorting) = withContext(dispatcher) {
        settings.putString(SettingsKeys.SubredditPostSorting, value.name)
    }

    override suspend fun setSearchPostSorting(value: SearchPostSorting) = withContext(dispatcher) {
        settings.putString(SettingsKeys.SearchPostSorting, value.name)
    }
}