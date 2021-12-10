package com.rainbow.app.settings

import com.rainbow.app.model.Model
import com.rainbow.data.Repos
import com.rainbow.domain.models.PostLayout
import com.rainbow.domain.models.Theme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

object SettingsModel : Model() {

    val isSidebarExpanded = Repos.Settings.isSidebarExpanded.stateIn(scope, SharingStarted.Eagerly, true)

    val isUserLoggedIn = Repos.User.isUserLoggedIn.stateIn(scope, SharingStarted.Eagerly, false)

    val isPostFullHeight = Repos.Settings.isFullHeight.stateIn(scope, SharingStarted.Eagerly, false)

    val theme = Repos.Settings.theme.stateIn(scope, SharingStarted.Eagerly, Theme.System)

    val postLayout = Repos.Settings.postLayout.stateIn(scope, SharingStarted.Eagerly, PostLayout.Card)

}