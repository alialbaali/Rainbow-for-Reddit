package com.rainbow.app

import androidx.compose.desktop.Window
import androidx.compose.ui.unit.IntSize
import com.rainbow.app.utils.RainbowStrings
import java.io.File
import javax.imageio.ImageIO

private val WindowSize = IntSize(1330, 870)
private val ComposeIcon = ImageIO.read(File("C:\\Users\\ali\\Downloads\\compose.png"))

fun main() = Window(
    title = RainbowStrings.Rainbow,
    size = WindowSize,
    icon = ComposeIcon,
) { Rainbow() }