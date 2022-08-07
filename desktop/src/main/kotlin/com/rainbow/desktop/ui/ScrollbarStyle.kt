package com.rainbow.desktop.ui

import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp


val DefaultScrollbarStyle
    @Composable
    get() = ScrollbarStyle(
        minimalHeight = 100.dp,
        thickness = 8.dp,
        shape = CircleShape,
        hoverDurationMillis = 300,
        unhoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        hoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.50f)
    )