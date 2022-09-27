package com.rainbow.desktop.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.ui.RainbowColors

@Composable
fun BoxLine(
    index: Int,
    modifier: Modifier = Modifier,
) {
    val color = remember(index) {
        if (index < RainbowColors.size) {
            RainbowColors[index]
        } else {
            RainbowColors[index - RainbowColors.size]
        }
    }
    Box(
        modifier
            .background(color)
            .width(5.dp)
            .fillMaxHeight(),
    )
    Spacer(Modifier.width(16.dp))
}