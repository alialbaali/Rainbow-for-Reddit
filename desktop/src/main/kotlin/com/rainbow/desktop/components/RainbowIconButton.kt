package com.rainbow.desktop.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import com.rainbow.desktop.ui.RainbowTheme

@Composable
fun RainbowIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified,
    hoverContainerColor: Color = Color.Unspecified,
    hoverContentColor: Color = Color.Unspecified,
    shape: Shape = MaterialTheme.shapes.medium,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val appliedContainerColor by animateColorAsState(if (isHovered) hoverContainerColor else containerColor)
    val appliedContentColor by animateColorAsState(if (isHovered) hoverContentColor else contentColor)

    Box(
        modifier
            .clip(shape)
            .hoverable(interactionSource)
            .background(appliedContainerColor, shape)
            .clickable(
                interactionSource,
                indication = rememberRipple(bounded = false),
                role = Role.Button,
                onClick = onClick,
            )
            .padding(RainbowTheme.dpDimensions.medium),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides appliedContentColor, content = content)
    }
}

@Composable
fun RainbowIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified,
    checkedContainerColor: Color = Color.Unspecified,
    checkedContentColor: Color = Color.Unspecified,
    hoverContainerColor: Color = Color.Unspecified,
    hoverContentColor: Color = Color.Unspecified,
    shape: Shape = MaterialTheme.shapes.medium,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val appliedCheckedContainerColor by animateColorAsState(if (checked) checkedContainerColor else containerColor)
    val appliedCheckedContentColor by animateColorAsState(if (checked) checkedContentColor else contentColor)
    val appliedContainerColor by animateColorAsState(if (isHovered) hoverContainerColor else appliedCheckedContainerColor)
    val appliedContentColor by animateColorAsState(if (isHovered) hoverContentColor else appliedCheckedContentColor)

    Box(
        modifier
            .clip(shape)
            .hoverable(interactionSource)
            .background(appliedContainerColor, shape)
            .toggleable(
                checked,
                interactionSource,
                indication = rememberRipple(bounded = false),
                role = Role.Checkbox,
                onValueChange = onCheckedChange,
            )
            .padding(RainbowTheme.dpDimensions.medium),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides appliedContentColor, content = content)
    }
}