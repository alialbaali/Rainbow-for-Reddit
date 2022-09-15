package com.rainbow.desktop.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.DropdownMenu
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings

private val DropdownMenuMaxHeight = 300.dp
private val DropdownMenuMinHeight = 200.dp
private val DropDownMenuMinWidth = 200.dp

@Composable
fun RainbowDropdownMenuHolder(
    onClick: () -> Unit,
    text: @Composable () -> Unit,
    icon: (@Composable () -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconOnly: Boolean = false,
    containerColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable ColumnScope.(DropdownMenuHandler) -> Unit,
) {
    var isVisible by remember { mutableStateOf(false) }
    val iconRotation by animateFloatAsState(if (isVisible) 180F else 0F)
    val scope = remember {
        object : DropdownMenuHandler {
            override fun hideMenu() {
                isVisible = false
            }
        }
    }
    Column(modifier) {
        if (iconOnly) {
            RainbowIconButton(onClick) {
                if (icon != null) icon()
            }
        } else {
            RainbowButton(
                onClick = {
                    isVisible = !isVisible
                    onClick()
                },
                modifier = modifier,
                enabled = enabled,
                containerColor = containerColor,
            ) {
                if (icon != null) icon()
                text()
                Icon(RainbowIcons.ExpandMore, RainbowStrings.Show, Modifier.rotate(iconRotation))
            }
        }

        DropdownMenu(
            isVisible,
            onDismissRequest = { isVisible = false },
            modifier = Modifier
                .sizeIn(
                    minWidth = DropDownMenuMinWidth,
                    minHeight = DropdownMenuMinHeight,
                    maxHeight = DropdownMenuMaxHeight,
                )
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = RainbowTheme.dpDimensions.small),
            offset = DpOffset(0.dp, 16.dp)
        ) {
            content(scope)
        }
    }
}

@Composable
fun RainbowDropdownMenuItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = RainbowTheme.dpDimensions.medium)
            .clip(MaterialTheme.shapes.small)
            .selectable(selected, enabled, onClick = onClick)
            .background(if (selected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surface)
            .padding(RainbowTheme.dpDimensions.medium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
        Spacer(Modifier.width(RainbowTheme.dpDimensions.medium))
        if (selected) {
            Icon(RainbowIcons.CheckCircleOutline, contentDescription = null)
        }
    }
}

interface DropdownMenuHandler {
    fun hideMenu()
}