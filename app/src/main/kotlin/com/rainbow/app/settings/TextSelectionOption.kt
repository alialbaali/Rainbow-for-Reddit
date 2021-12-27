package com.rainbow.app.settings

import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.rainbow.app.utils.RainbowStrings

@Composable
fun TextSelectionOption(modifier: Modifier = Modifier) {
    val isTextSelectionEnabled by SettingsModel.isTextSelectionEnabled.collectAsState()
    SettingsOption(RainbowStrings.TextSelection) {
        Switch(
            isTextSelectionEnabled,
            SettingsModel::setIsTextSelectionEnabled,
        )
    }
}