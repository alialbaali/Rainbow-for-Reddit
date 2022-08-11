package com.rainbow.desktop.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Dot(modifier: Modifier = Modifier, color: Color = MaterialTheme.colorScheme.surfaceVariant) {
    Text("•", modifier.padding(horizontal = 8.dp), color = color)
}