package com.rainbow.desktop

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.window.awaitApplication
import com.rainbow.desktop.app.AppScreen
import com.rainbow.desktop.components.RainbowWindow
import com.rainbow.desktop.login.LoginScreen
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.RainbowStrings

suspend fun main() = Rainbow()

private suspend fun Rainbow() = awaitApplication {
    val stateHolder = remember { RainbowStateHolder() }
    val theme by stateHolder.theme.collectAsState()
    val isUserLoggedIn by stateHolder.isUserLoggedIn.collectAsState()
    RainbowWindow(RainbowStrings.Rainbow, stateHolder::refreshContent) {
        RainbowTheme(theme) {
            Crossfade(isUserLoggedIn) { isUserLoggedIn ->
                when (isUserLoggedIn) {
                    true -> AppScreen()
                    false -> LoginScreen()
                    null -> SplashScreen()
                }
            }
        }
    }
}