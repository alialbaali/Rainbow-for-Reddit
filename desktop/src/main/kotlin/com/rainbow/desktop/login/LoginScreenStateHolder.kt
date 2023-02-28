package com.rainbow.desktop.login

import com.rainbow.data.Repos
import com.rainbow.desktop.state.StateHolder
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.toUIState
import com.rainbow.domain.repository.SettingsRepository
import com.rainbow.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.*

class LoginScreenStateHolder(
    private val userRepository: UserRepository = Repos.User,
    private val settingsRepository: SettingsRepository = Repos.Settings,
) : StateHolder() {

    private val uuid = UUID.randomUUID()

    val isUserLoggedIn = userRepository.isUserLoggedIn.stateIn(scope, SharingStarted.Eagerly, null)

    private val mutableState = MutableStateFlow<UIState<Unit>>(UIState.Empty)
    val state get() = mutableState.asStateFlow()

    fun createAuthenticationUrl() = userRepository.createAuthenticationUrl(uuid)

    fun loginUser() = scope.launch {
        mutableState.value = UIState.Loading()
        mutableState.value = userRepository.loginUser(uuid)
            .onSuccess {
                launch {
                    userRepository.getCurrentUser()
                }
            }
            .onFailure { throw it }
            .toUIState()
    }


    companion object {
        val Instance = LoginScreenStateHolder()
    }

}