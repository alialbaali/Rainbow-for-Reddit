package com.rainbow.desktop.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit

@Composable
fun TextBox(
    text: String,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.inverseOnSurface,
) {
    Box(modifier, Alignment.Center) {
        Text(
            text.first().toString().uppercase(),
            color = color,
            fontSize = fontSize,
        )
    }
}