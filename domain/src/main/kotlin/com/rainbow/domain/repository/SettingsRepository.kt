package com.rainbow.domain.repository

import com.rainbow.domain.models.*
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    val theme: Flow<Theme>

    val postLayout: Flow<PostLayout>

    val profilePostSorting: Flow<ProfilePostSorting>

    val homePostSorting: Flow<HomePostSorting>

    val subredditPostSorting: Flow<SubredditPostSorting>

    val searchPostSorting: Flow<SearchPostSorting>

    val userPostSorting: Flow<UserPostSorting>

    val postCommentSorting: Flow<PostCommentSorting>

    val isCommentsCollapsed: Flow<Boolean>

    val isTextSelectionEnabled: Flow<Boolean>

    val markPostAsRead: Flow<MarkPostAsRead>

    fun getHomePostSorting(): HomePostSorting

    fun getProfilePostSorting(): ProfilePostSorting

    fun getUserPostSorting(): UserPostSorting

    fun getSubredditPostSorting(): SubredditPostSorting

    fun getSearchPostSorting(): SearchPostSorting

    fun getPostCommentSorting(): PostCommentSorting

    fun getIsCommentsCollapsed(): Boolean

    suspend fun setTheme(theme: Theme)

    suspend fun setPostLayout(value: PostLayout)

    suspend fun setHomePostSorting(value: HomePostSorting)

    suspend fun setUserPostSorting(value: UserPostSorting)

    suspend fun setProfilePostSorting(value: ProfilePostSorting)

    suspend fun setSubredditPostSorting(value: SubredditPostSorting)

    suspend fun setSearchPostSorting(value: SearchPostSorting)

    suspend fun setPostCommentSorting(value: PostCommentSorting)

    suspend fun setIsCommentsCollapsed(value: Boolean)

    suspend fun setIsTextSelectionEnabled(value: Boolean)

    suspend fun setMarkPostAsRead(value: MarkPostAsRead)
}