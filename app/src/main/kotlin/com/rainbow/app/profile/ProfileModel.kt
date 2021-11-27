package com.rainbow.app.profile

import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object ProfileModel {
    val scope = ModelScope()

    private val mutableCurrentUser = MutableStateFlow<UIState<User>>(UIState.Loading)
    val currentUser get() = mutableCurrentUser.asStateFlow()

    init {
        scope.launch {
            mutableCurrentUser.value = Repos.User.getCurrentUser().toUIState()
        }
    }
}

fun ModelScope() = CoroutineScope(Dispatchers.IO + SupervisorJob())