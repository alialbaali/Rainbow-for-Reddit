package com.rainbow.app.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.rainbow.app.components.DropdownMenuHolder
import com.rainbow.app.utils.RainbowStrings

@Composable
fun MarkPostAsReadOption(modifier: Modifier = Modifier) {
    val markPostAsRead by SettingsModel.markPostAsRead.collectAsState()
    SettingsOption(RainbowStrings.MarkPostAsRead, modifier) {
        DropdownMenuHolder(markPostAsRead, SettingsModel::setMarkPostAsRead)
    }
}