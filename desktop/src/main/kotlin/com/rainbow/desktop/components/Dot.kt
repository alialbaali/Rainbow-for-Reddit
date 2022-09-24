package com.rainbow.desktop.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.ui.RainbowTheme

private val DotSize = 5.dp

@Composable
fun Dot(modifier: Modifier = Modifier) {
    Box(
        modifier
            .padding(horizontal = RainbowTheme.dimensions.small)
            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
            .size(DotSize)
    )
}