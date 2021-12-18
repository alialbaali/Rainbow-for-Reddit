package com.rainbow.app.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit

@Composable
fun TextBox(text: String, fontSize: TextUnit, modifier: Modifier = Modifier) {
    Box(modifier, Alignment.Center) {
        Text(
            text.first().toString().uppercase(),
            color = MaterialTheme.colors.onSecondary,
            fontSize = fontSize,
        )
    }
}