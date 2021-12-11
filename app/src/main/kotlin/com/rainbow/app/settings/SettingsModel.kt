package com.rainbow.app.settings

import com.rainbow.app.model.Model
import com.rainbow.data.Repos
import com.rainbow.domain.models.PostLayout
import com.rainbow.domain.models.Theme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.*

object SettingsModel : Model() {

    val isSidebarExpanded = Repos.Settings.isSidebarExpanded.stateIn(scope, SharingStarted.Eagerly, true)

    val isUserLoggedIn = Repos.User.isUserLoggedIn.stateIn(scope, SharingStarted.Eagerly, false)

    val isPostFullHeight = Repos.Settings.isFullHeight.stateIn(scope, SharingStarted.Eagerly, false)

    val theme = Repos.Settings.theme.stateIn(scope, SharingStarted.Eagerly, Theme.System)

    val postLayout = Repos.Settings.postLayout.stateIn(scope, SharingStarted.Eagerly, PostLayout.Card)

    fun setIsSidebarExpanded(value: Boolean) = scope.launch {
        Repos.Settings.setIsSidebarExpanded(value)
    }

    fun loginUser(uuid: UUID, onSuccess: () -> Unit, onFailure: (Throwable) -> Unit) = scope.launch {
        Repos.User.loginUser(uuid)
            .onSuccess {
                onSuccess()
                Repos.User.getCurrentUser()
            }
            .onFailure(onFailure)
    }

    fun logoutUser() = scope.launch {
        Repos.User.logoutUser()
    }

    fun setIsPostFullHeight(value: Boolean) = scope.launch {
        Repos.Settings.setIsPostFullHeight(value)
    }

    fun setPostLayout(value: PostLayout) = scope.launch {
        Repos.Settings.setPostLayout(value)
    }

    fun setTheme(value: Theme) = scope.launch {
        Repos.Settings.setTheme(value)
    }
}