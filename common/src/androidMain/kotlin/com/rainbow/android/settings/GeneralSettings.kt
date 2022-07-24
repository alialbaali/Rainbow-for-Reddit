package com.rainbow.android.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rainbow.android.navigation.Screen
import com.rainbow.common.components.LogoutButton
import com.rainbow.common.settings.SettingsModel
import com.rainbow.common.utils.RainbowStrings

@Composable
fun GeneralSettings(onNavigate: (Screen.Sheet) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier) {
        SettingsOption(RainbowStrings.Theme, onClick = { onNavigate(Screen.Sheet.Theme) })
        LogoutButton(SettingsModel::logoutUser)
    }
}