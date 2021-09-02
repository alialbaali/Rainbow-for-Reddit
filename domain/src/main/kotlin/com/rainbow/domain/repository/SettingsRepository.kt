package com.rainbow.domain.repository

import com.rainbow.domain.models.Theme
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    val theme: Flow<Theme>

    suspend fun setTheme(theme: Theme)

}