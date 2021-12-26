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

    override val profilePostSorting: Flow<ProfilePostSorting> = flowSettings
        .getStringOrNullFlow(SettingsKeys.ProfilePostSorting)
        .map { if (it != null) ProfilePostSorting.valueOf(it) else ProfilePostSorting.Default }

    override val homePostSorting: Flow<HomePostSorting> = flowSettings.getStringOrNullFlow(SettingsKeys.HomePostSorting)
        .map { if (it != null) HomePostSorting.valueOf(it) else HomePostSorting.Default }

    override val subredditPostSorting: Flow<SubredditPostSorting> = flowSettings
        .getStringOrNullFlow(SettingsKeys.SubredditPostSorting)
        .map { if (it != null) SubredditPostSorting.valueOf(it) else SubredditPostSorting.Default }

    override val searchPostSorting: Flow<SearchPostSorting> =
        flowSettings.getStringOrNullFlow(SettingsKeys.SearchPostSorting)
            .map { if (it != null) SearchPostSorting.valueOf(it) else SearchPostSorting.Default }

    override val userPostSorting: Flow<UserPostSorting> = flowSettings.getStringOrNullFlow(SettingsKeys.UserPostSorting)
        .map { if (it != null) UserPostSorting.valueOf(it) else UserPostSorting.Default }

    override val postCommentSorting: Flow<PostCommentSorting> =
        flowSettings.getStringOrNullFlow(SettingsKeys.PostCommentSorting)
            .map { if (it != null) PostCommentSorting.valueOf(it) else PostCommentSorting.Default }

    override val isCommentsCollapsed: Flow<Boolean> =
        flowSettings.getBooleanOrNullFlow(SettingsKeys.IsCommentsCollapsed)
            .map { it ?: false }

    override fun getHomePostSorting(): HomePostSorting = settings.getStringOrNull(SettingsKeys.HomePostSorting)
        .let { if (it != null) HomePostSorting.valueOf(it) else HomePostSorting.Default }

    override fun getProfilePostSorting(): ProfilePostSorting = settings.getStringOrNull(SettingsKeys.ProfilePostSorting)
        .let { if (it != null) ProfilePostSorting.valueOf(it) else ProfilePostSorting.Default }

    override fun getUserPostSorting(): UserPostSorting = settings.getStringOrNull(SettingsKeys.UserPostSorting)
        .let { if (it != null) UserPostSorting.valueOf(it) else UserPostSorting.Default }

    override fun getSubredditPostSorting(): SubredditPostSorting = settings.getStringOrNull(SettingsKeys.SubredditPostSorting)
        .let { if (it != null) SubredditPostSorting.valueOf(it) else SubredditPostSorting.Default }

    override fun getSearchPostSorting(): SearchPostSorting = settings.getStringOrNull(SettingsKeys.SearchPostSorting)
        .let { if (it != null) SearchPostSorting.valueOf(it) else SearchPostSorting.Default }

    override fun getPostCommentSorting(): PostCommentSorting = settings.getStringOrNull(SettingsKeys.PostCommentSorting)
        .let { if (it != null) PostCommentSorting.valueOf(it) else PostCommentSorting.Default }

    override fun getIsCommentsCollapsed(): Boolean =
        settings.getBooleanOrNull(SettingsKeys.IsCommentsCollapsed) ?: false

    override suspend fun setTheme(theme: Theme) = withContext(dispatcher) {
        flowSettings.putString(SettingsKeys.Theme, theme.name)
    }

    override suspend fun setPostLayout(value: PostLayout) = withContext(dispatcher) {
        flowSettings.putString(SettingsKeys.PostLayout, value.name)
    }

    override suspend fun setIsPostFullHeight(value: Boolean) = withContext(dispatcher) {
        flowSettings.putBoolean(SettingsKeys.IsPostFullHeight, value)
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

    override suspend fun setPostCommentSorting(value: PostCommentSorting) = withContext(dispatcher) {
        flowSettings.putString(SettingsKeys.PostCommentSorting, value.name)
    }

    override suspend fun setIsCommentsCollapsed(value: Boolean) = withContext(dispatcher) {
        settings.putBoolean(SettingsKeys.IsCommentsCollapsed, value)
    }
}