package com.rainbow.desktop

import com.rainbow.data.Repos
import com.rainbow.desktop.app.AppScreenStateHolder
import com.rainbow.desktop.state.StateHolder
import com.rainbow.domain.models.Theme
import com.rainbow.domain.repository.SettingsRepository
import com.rainbow.domain.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class RainbowStateHolder(
    private val userRepository: UserRepository = Repos.User,
    private val settingsRepository: SettingsRepository = Repos.Settings,
) : StateHolder() {

    val isUserLoggedIn = userRepository.isUserLoggedIn.stateIn(scope, SharingStarted.Eagerly, null)

    val theme = settingsRepository.theme.stateIn(scope, SharingStarted.Eagerly, Theme.System)

    fun refreshContent() = AppScreenStateHolder.refreshContent()

}