package com.rainbow.domain.repository

import com.rainbow.domain.models.PostLayout
import com.rainbow.domain.models.Theme
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    val isSidebarExpanded: Flow<Boolean>

    val theme: Flow<Theme>

    val isFullHeight: Flow<Boolean>

    val postLayout: Flow<PostLayout>

    suspend fun setTheme(theme: Theme)

    suspend fun setIsPostFullHeight(value: Boolean)

    suspend fun setPostLayout(value: PostLayout)

    suspend fun setIsSidebarExpanded(value: Boolean)

}