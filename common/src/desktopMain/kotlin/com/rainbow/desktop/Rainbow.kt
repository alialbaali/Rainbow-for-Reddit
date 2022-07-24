package com.rainbow.desktop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.rainbow.common.login.LoginScreen
import com.rainbow.common.settings.SettingsModel
import com.rainbow.desktop.ui.RainbowTheme

@Composable
fun Rainbow() = RainbowTheme {
    val isUserLoggedIn by SettingsModel.isUserLoggedIn.collectAsState()
    if (isUserLoggedIn)
        App()
    else
        LoginScreen()
}