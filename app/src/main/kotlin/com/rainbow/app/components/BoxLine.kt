package com.rainbow.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rainbow.app.ui.RainbowColors

@Composable
fun RowScope.BoxLine(
    index: Int,
    height: Dp,
    modifier: Modifier = Modifier,
) {
    val color = RainbowColors.getOrNull(index) ?: RainbowColors[0]
    Box(
        modifier
            .background(color)
            .width(5.dp)
            .height(height),
    )
    Spacer(Modifier.width(16.dp))
}