package com.rainbow.app.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

@Composable
fun SettingsScreen() {
    Column {
        ThemeOption()
        PostFullHeightOption()
        PostLayoutOption()
        LogoutButton()
    }
}