package com.rainbow.desktop.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.ui.RainbowTheme

private val MinTextWidth = 280.dp
private val TrailingIconSize = 40.dp
private val TextStartPadding
    @Composable
    get() = RainbowTheme.dimensions.medium
private val SpacerWidth
    @Composable
    get() = RainbowTheme.dimensions.small

@Composable
fun RainbowTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    singleLine: Boolean = true,
    maxLines: Int = 1,
) {
    val isPlaceholderVisible by remember(value) { mutableStateOf(value.isBlank()) }
    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        value,
        onValueChange,
        modifier
            .widthIn(min = MinTextWidth)
            .background(backgroundColor, MaterialTheme.shapes.small)
            .padding(start = TextStartPadding),
        singleLine = singleLine,
        maxLines = maxLines,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = contentColor),
    ) { innerTextField ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.widthIn(max = MinTextWidth - TrailingIconSize - TextStartPadding - SpacerWidth),
                contentAlignment = Alignment.CenterStart
            ) {
                innerTextField()
                if (isPlaceholderVisible) {
                    Text(
                        placeholderText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = contentColor,
                    )
                }
            }
            Spacer(Modifier.width(SpacerWidth))
            Box(Modifier.size(TrailingIconSize)) {
                if (trailingIcon != null) {
                    trailingIcon()
                }
            }
        }
    }
}