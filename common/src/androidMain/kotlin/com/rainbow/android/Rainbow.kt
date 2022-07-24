package com.rainbow.android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.rainbow.android.ui.RainbowTheme
import com.rainbow.common.login.LoginScreen
import com.rainbow.common.settings.SettingsModel

@Composable
fun Rainbow() = RainbowTheme {
    val isUserLoggedIn by SettingsModel.isUserLoggedIn.collectAsState()
    if (isUserLoggedIn)
        App()
    else
        LoginScreen()
}