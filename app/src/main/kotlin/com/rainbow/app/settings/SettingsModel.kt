package com.rainbow.app.settings

import com.rainbow.app.model.Model
import com.rainbow.data.Repos
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

object SettingsModel : Model() {

    val isSidebarExpanded = Repos.Settings.isSidebarExpanded.stateIn(scope, SharingStarted.Lazily, true)

}