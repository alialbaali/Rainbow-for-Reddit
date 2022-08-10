package com.rainbow.desktop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.rainbow.desktop.login.LoginScreen
import com.rainbow.desktop.settings.SettingsStateHolder
import com.rainbow.desktop.ui.RainbowTheme

@Composable
fun Rainbow() = RainbowTheme {
    val isUserLoggedIn by SettingsStateHolder.isUserLoggedIn.collectAsState()
    if (isUserLoggedIn)
        App()
    else
        LoginScreen()
}