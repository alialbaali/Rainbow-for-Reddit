package com.rainbow.app.settings

import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.rainbow.app.utils.RainbowStrings

@Composable
fun PostFullHeightOption(modifier: Modifier = Modifier) {
    val isPostFulLHeight by SettingsModel.isPostFullHeight.collectAsState()
    SettingsOption(RainbowStrings.PostFullHeight, modifier) {
        Switch(
            isPostFulLHeight,
            onCheckedChange = { isPostFullHeight -> SettingsModel.setIsPostFullHeight(isPostFullHeight) },
        )
    }
}