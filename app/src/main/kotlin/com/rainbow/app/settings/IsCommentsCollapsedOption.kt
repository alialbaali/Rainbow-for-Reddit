package com.rainbow.app.settings

import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.rainbow.app.utils.RainbowStrings

@Composable
fun IsCommentsCollapsedOption() {
    val isCommentsCollapsed by SettingsModel.isCommentsCollapsed.collectAsState()
    SettingsOption(RainbowStrings.IsCommentsCollapsed) {
        Switch(
            isCommentsCollapsed,
            onCheckedChange = { SettingsModel.setIsCommentsCollapsed(it) },
        )
    }
}