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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.ui.RainbowTheme

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
    val textStartPaddding = RainbowTheme.dpDimensions.medium
    var trailingIconWidth by remember { mutableStateOf(Dp.Unspecified) }
    BasicTextField(
        value,
        onValueChange,
        modifier
            .defaultMinSize(minTextWidth)
            .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.small)
            .padding(start = textStartPaddding)
            .then(
                if (trailingIcon != null)
                    Modifier
                else
                    Modifier.padding(vertical = RainbowTheme.dpDimensions.medium)
            ),
        singleLine = singleLine,
        maxLines = maxLines,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
    ) { innerTextField ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(contentAlignment = Alignment.CenterStart) {
                Box(
                    Modifier
                        .widthIn(max = minTextWidth - trailingIconWidth - textStartPaddding)
                        .padding(end = RainbowTheme.dpDimensions.medium)
                ) {
                    innerTextField()
                }
                if (isPlaceholderVisible) {
                    Text(
                        placeholderText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
            Box(Modifier.onSizeChanged { trailingIconWidth = it.width.dp }) {
                if (trailingIcon != null) {
                    trailingIcon()
                }
            }
        }
    }
}