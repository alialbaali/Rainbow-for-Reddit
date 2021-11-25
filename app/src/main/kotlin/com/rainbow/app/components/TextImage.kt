package com.rainbow.app.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun TextImage(text: String, modifier: Modifier = Modifier, fontSize: TextUnit = 40.sp) {
    Box(modifier, Alignment.Center) {
        Text(
            text.first().toString(),
            color = MaterialTheme.colors.onSecondary,
            fontSize = fontSize
        )
    }
}