package com.rainbow.app

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.application
import com.rainbow.app.components.RainbowWindow
import com.rainbow.app.login.LoginScreen
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.data.Repos

fun main() = application {

    val isUserLoggedIn by Repos.User.isUserLoggedIn.collectAsState(false)

    RainbowWindow(RainbowStrings.Rainbow) {
        if (isUserLoggedIn)
            Rainbow()
        else
            LoginScreen()
    }
}