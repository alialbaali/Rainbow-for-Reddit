package com.rainbow.app.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun GeneralSettings(modifier: Modifier = Modifier) {
    SettingsTabContent(modifier) {
        ThemeOption()
        LogoutButton()
    }
}