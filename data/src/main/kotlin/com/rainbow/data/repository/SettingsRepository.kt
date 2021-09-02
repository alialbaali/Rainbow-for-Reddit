package com.rainbow.data.repository

import com.rainbow.domain.models.Theme
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
    dispatcher: CoroutineDispatcher
): SettingsRepository = SettingsRepositoryImpl(settings, dispatcher)

@OptIn(ExperimentalSettingsApi::class)
private class SettingsRepositoryImpl(
    private val settings: FlowSettings,
    private val dispatcher: CoroutineDispatcher
) : SettingsRepository {

    override val theme: Flow<Theme> = settings.getStringFlow(SettingsDefaults.Theme, Theme.System.name)
        .map { Theme.valueOf(it) }

    override suspend fun setTheme(theme: Theme) = withContext(dispatcher) {
        settings.putString(SettingsDefaults.Theme, theme.name)
    }
}

private object SettingsDefaults {
    const val Theme = "Theme"
}