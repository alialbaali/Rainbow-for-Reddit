package com.rainbow.app.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.rainbow.app.components.DropdownMenuHolder
import com.rainbow.app.utils.RainbowStrings

@Composable
fun PostLayoutOption(modifier: Modifier = Modifier) {
    val postLayout by SettingsModel.postLayout.collectAsState()
    SettingsOption(RainbowStrings.PostLayout, modifier) {
        DropdownMenuHolder(postLayout, SettingsModel::setPostLayout)
    }
}