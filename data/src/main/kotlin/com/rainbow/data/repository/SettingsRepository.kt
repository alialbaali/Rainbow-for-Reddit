package com.rainbow.data.repository

import com.rainbow.data.utils.SettingsKeys
import com.rainbow.domain.models.*
import com.rainbow.domain.repository.SettingsRepository
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@OptIn(ExperimentalSettingsApi::class)
internal class SettingsRepositoryImpl(
    private val settings: Settings,
    private val flowSettings: FlowSettings,
    private val dispatcher: CoroutineDispatcher,
) : SettingsRepository {

    override val isSidebarExpanded: Flow<Boolean> = flowSettings.getBooleanFlow(SettingsKeys.IsSidebarExpanded)

    override val theme: Flow<Theme> = flowSettings.getStringFlow(SettingsKeys.Theme, Theme.System.name)
        .map { Theme.valueOf(it) }

    override val isFullHeight: Flow<Boolean> = flowSettings.getBooleanFlow(SettingsKeys.IsPostFullHeight)

    override val postLayout: Flow<PostLayout> =
        flowSettings.getStringFlow(SettingsKeys.PostLayout, PostLayout.Card.name)
            .map { PostLayout.valueOf(it) }

    override val profilePostSorting: Flow<ProfilePostSorting> =
        flowSettings.getStringFlow(SettingsKeys.ProfilePostSorting)
            .map { ProfilePostSorting.valueOf(it) }

    override val homePostSorting: Flow<HomePostSorting> = flowSettings.getStringFlow(SettingsKeys.HomePostSorting)
        .map { HomePostSorting.valueOf(it) }

    override val subredditPostSorting: Flow<SubredditPostSorting> =
        flowSettings.getStringFlow(SettingsKeys.SubredditPostSorting)
            .map { SubredditPostSorting.valueOf(it) }

    override suspend fun setIsPostFullHeight(value: Boolean) = withContext(dispatcher) {
        flowSettings.putBoolean(SettingsKeys.IsPostFullHeight, value)
    }

    override val searchPostSorting: Flow<SearchPostSorting> = flowSettings.getStringFlow(SettingsKeys.SearchPostSorting)
        .map { SearchPostSorting.valueOf(it) }

    override val userPostSorting: Flow<UserPostSorting> = flowSettings.getStringFlow(SettingsKeys.UserPostSorting)
        .map { UserPostSorting.valueOf(it) }

    override fun getHomePostSorting(): HomePostSorting = settings.getString(SettingsKeys.HomePostSorting)
        .let { HomePostSorting.valueOf(it) }

    override fun getProfilePostSorting(): ProfilePostSorting = settings.getString(SettingsKeys.ProfilePostSorting)
        .let { ProfilePostSorting.valueOf(it) }

    override fun getUserPostSorting(): UserPostSorting = settings.getString(SettingsKeys.UserPostSorting)
        .let { UserPostSorting.valueOf(it) }

    override fun getSubredditPostSorting(): SubredditPostSorting = settings.getString(SettingsKeys.SubredditPostSorting)
        .let { SubredditPostSorting.valueOf(it) }

    override fun getSearchPostSorting(): SearchPostSorting = settings.getString(SettingsKeys.SearchPostSorting)
        .let { SearchPostSorting.valueOf(it) }

    override suspend fun setTheme(theme: Theme) = withContext(dispatcher) {
        flowSettings.putString(SettingsKeys.Theme, theme.name)
    }

    override suspend fun setPostLayout(value: PostLayout) = withContext(dispatcher) {
        flowSettings.putString(SettingsKeys.PostLayout, value.name)
    }

    override suspend fun setIsSidebarExpanded(value: Boolean) = withContext(dispatcher) {
        flowSettings.putBoolean(SettingsKeys.IsSidebarExpanded, value)
    }

    override suspend fun setHomePostSorting(value: HomePostSorting) = withContext(dispatcher) {
        flowSettings.putString(SettingsKeys.HomePostSorting, value.name)
    }

    override suspend fun setUserPostSorting(value: UserPostSorting) = withContext(dispatcher) {
        flowSettings.putString(SettingsKeys.UserPostSorting, value.name)
    }

    override suspend fun setProfilePostSorting(value: ProfilePostSorting) = withContext(dispatcher) {
        flowSettings.putString(SettingsKeys.ProfilePostSorting, value.name)
    }

    override suspend fun setSubredditPostSorting(value: SubredditPostSorting) = withContext(dispatcher) {
        flowSettings.putString(SettingsKeys.SubredditPostSorting, value.name)
    }

    override suspend fun setSearchPostSorting(value: SearchPostSorting) = withContext(dispatcher) {
        flowSettings.putString(SettingsKeys.SearchPostSorting, value.name)
    }
}