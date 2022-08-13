package com.rainbow.desktop

import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.rainbow.desktop.utils.RainbowStrings

fun main() = application {
    RainbowWindow(RainbowStrings.Rainbow) {
        Rainbow()
    }
}