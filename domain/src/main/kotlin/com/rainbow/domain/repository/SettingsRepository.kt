package com.rainbow.domain.repository

import com.rainbow.domain.models.*
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    val isSidebarExpanded: Flow<Boolean>

    val theme: Flow<Theme>

    val isFullHeight: Flow<Boolean>

    val postLayout: Flow<PostLayout>

    val profilePostSorting: Flow<ProfilePostSorting>

    val homePostSorting: Flow<HomePostSorting>

    val subredditPostSorting: Flow<SubredditPostSorting>

    val searchPostSorting: Flow<SearchPostSorting>

    val userPostSorting: Flow<UserPostSorting>

    suspend fun setTheme(theme: Theme)

    suspend fun setIsPostFullHeight(value: Boolean)

    suspend fun setPostLayout(value: PostLayout)

    suspend fun setIsSidebarExpanded(value: Boolean)

    suspend fun setHomePostSorting(value: HomePostSorting)

    suspend fun setUserPostSorting(value: UserPostSorting)

    suspend fun setProfilePostSorting(value: ProfilePostSorting)

    suspend fun setSubredditPostSorting(value: SubredditPostSorting)

    suspend fun setSearchPostSorting(value: SearchPostSorting)
}