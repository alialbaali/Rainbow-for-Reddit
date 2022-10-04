package com.rainbow.desktop.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

enum class LetterBoxSize {
    Small,
    Medium,
    Large,
    ExtraLarge,
}

@Composable
fun LetterBox(
    text: String,
    size: LetterBoxSize,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.inverseOnSurface,
) {
    val fontSize = remember(size) {
        when (size) {
            LetterBoxSize.Small -> 15.sp
            LetterBoxSize.Medium -> 30.sp
            LetterBoxSize.Large -> 40.sp
            LetterBoxSize.ExtraLarge -> 150.sp
        }
    }

    val letter = remember(text) { text.first().toString().uppercase() }

    Box(modifier, Alignment.Center) {
        Text(
            letter,
            color = color,
            fontSize = fontSize,
        )
    }
}