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

@Composable
fun Dot(modifier: Modifier = Modifier) {
    Box(
        modifier
            .padding(8.dp)
            .size(5.dp)
            .background(MaterialTheme.colorScheme.onBackground.copy(0.15F), CircleShape)
    )
}