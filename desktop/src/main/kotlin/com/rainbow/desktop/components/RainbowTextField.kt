package com.rainbow.desktop.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.desktop.utils.defaultPadding

@Composable
fun RainbowTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    maxLines: Int = 1,
) {
    val isPlaceholderVisible by remember(value) { mutableStateOf(value.isBlank()) }
    val interactionSource = remember { MutableInteractionSource() }
    val minTextWidth = 280.dp
    val textStartPadding = 16.dp
    var trailingIconWidth by remember { mutableStateOf(Dp.Unspecified) }
    val textStyle = TextStyle(fontSize = 16.sp)
    BasicTextField(
        value,
        onValueChange,
        modifier
            .defaultMinSize(minTextWidth)
            .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium)
            .defaultPadding(start = textStartPadding)
            .then(
                if (trailingIcon != null)
                    Modifier
                else
                    Modifier.padding(vertical = 16.dp)
            ),
        singleLine = singleLine,
        maxLines = maxLines,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        textStyle = textStyle
    ) { innerTextField ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(contentAlignment = Alignment.CenterStart) {
                Box(
                    Modifier.widthIn(max = minTextWidth - trailingIconWidth - textStartPadding)
                        .padding(end = 16.dp)
                ) {
                    innerTextField()
                }
                if (isPlaceholderVisible)
                    Text(placeholderText, style = textStyle)
            }
            Box(Modifier.onSizeChanged { trailingIconWidth = it.width.dp }) {
                if (trailingIcon != null)
                    trailingIcon()
            }
        }
    }
}