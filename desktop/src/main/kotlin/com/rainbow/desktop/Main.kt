package com.rainbow.desktop

import androidx.compose.ui.window.application
import com.rainbow.common.utils.RainbowStrings

fun main() = application {
    RainbowWindow(RainbowStrings.Rainbow) {
        Rainbow()
    }
}