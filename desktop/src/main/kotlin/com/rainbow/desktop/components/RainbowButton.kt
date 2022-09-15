package com.rainbow.desktop.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
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
import com.rainbow.desktop.ui.dpDimensions

@Composable
fun RainbowButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    disabledContainerColor: Color = containerColor.copy(0.5F),
    disabledContentColor: Color = contentColor.copy(0.5F),
    hoverContainerColor: Color = containerColor.copy(0.5F),
    hoverContentColor: Color = contentColor,
    shape: Shape = MaterialTheme.shapes.small,
    content: @Composable RowScope.() -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val appliedContainerColor by animateColorAsState(
        if (enabled) {
            if (isHovered) {
                hoverContainerColor
            } else {
                containerColor
            }
        } else {
            disabledContainerColor
        }
    )
    val appliedContentColor by animateColorAsState(
        if (enabled) {
            if (isHovered) {
                hoverContentColor
            } else {
                contentColor
            }
        } else {
            disabledContentColor
        }
    )

    Row(
        modifier
            .clip(shape)
            .hoverable(interactionSource, enabled)
            .background(appliedContainerColor, shape)
            .clickable(
                interactionSource,
                indication = rememberRipple(bounded = false),
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(vertical = RainbowTheme.dpDimensions.small, horizontal = MaterialTheme.dpDimensions.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(RainbowTheme.dpDimensions.medium)
    ) {
        CompositionLocalProvider(
            LocalContentColor provides appliedContentColor,
            LocalTextStyle provides MaterialTheme.typography.labelLarge,
        ) {
            content()
        }
    }
}

@Composable
fun RainbowIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    disabledContainerColor: Color = containerColor.copy(0.5F),
    disabledContentColor: Color = contentColor.copy(0.5F),
    hoverContainerColor: Color = containerColor.copy(0.5F),
    hoverContentColor: Color = contentColor,
    shape: Shape = MaterialTheme.shapes.small,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val appliedContainerColor by animateColorAsState(
        if (enabled) {
            if (isHovered) {
                hoverContainerColor
            } else {
                containerColor
            }
        } else {
            disabledContainerColor
        }
    )
    val appliedContentColor by animateColorAsState(
        if (enabled) {
            if (isHovered) {
                hoverContentColor
            } else {
                contentColor
            }
        } else {
            disabledContentColor
        }
    )

    Box(
        modifier
            .clip(shape)
            .hoverable(interactionSource, enabled)
            .background(appliedContainerColor, shape)
            .clickable(
                interactionSource,
                indication = rememberRipple(bounded = false),
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(RainbowTheme.dpDimensions.small),
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
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    disabledContainerColor: Color = containerColor.copy(0.5F),
    disabledContentColor: Color = contentColor.copy(0.5F),
    checkedContainerColor: Color = MaterialTheme.colorScheme.primary,
    checkedContentColor: Color = contentColorFor(containerColor),
    hoverContainerColor: Color = containerColor.copy(0.5F),
    hoverContentColor: Color = contentColor,
    shape: Shape = MaterialTheme.shapes.small,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val appliedCheckedContainerColor by animateColorAsState(if (checked) checkedContainerColor else containerColor)
    val appliedCheckedContentColor by animateColorAsState(if (checked) checkedContentColor else contentColor)
    val appliedContainerColor by animateColorAsState(
        if (enabled) {
            if (isHovered) {
                hoverContainerColor
            } else {
                appliedCheckedContainerColor
            }
        } else {
            disabledContainerColor
        }
    )
    val appliedContentColor by animateColorAsState(
        if (enabled) {
            if (isHovered) {
                hoverContentColor
            } else {
                appliedCheckedContentColor
            }
        } else {
            disabledContentColor
        }
    )

    Box(
        modifier
            .clip(shape)
            .hoverable(interactionSource)
            .background(appliedContainerColor, shape)
            .toggleable(
                checked,
                interactionSource,
                indication = rememberRipple(bounded = false),
                enabled = enabled,
                role = Role.Checkbox,
                onValueChange = onCheckedChange,
            )
            .padding(RainbowTheme.dpDimensions.small),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides appliedContentColor, content = content)
    }
}