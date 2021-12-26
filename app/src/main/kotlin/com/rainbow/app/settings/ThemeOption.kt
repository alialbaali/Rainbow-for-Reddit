package com.rainbow.app.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.rainbow.app.components.DropdownMenuHolder
import com.rainbow.app.utils.RainbowStrings

@Composable
fun ThemeOption(modifier: Modifier = Modifier) {
    val theme by SettingsModel.theme.collectAsState()
    SettingsOption(RainbowStrings.Theme, modifier) {
        DropdownMenuHolder(theme, SettingsModel::setTheme)
    }
}