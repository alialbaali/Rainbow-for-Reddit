package com.rainbow.desktop.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.DropdownMenu
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings

private val DropDownMenuMinWidth = 200.dp
private val DropdownMenuMaxHeight = 300.dp
private val DropdownMenuMaxWidth = 400.dp
private val DropdownMenuOffset = DpOffset(0.dp, 16.dp)

@Composable
inline fun <reified T : Enum<T>> EnumDropdownMenuHolder(
    value: T,
    crossinline onValueUpdate: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    noinline icon: (@Composable () -> Unit)? = null,
    crossinline content: @Composable RowScope.(T) -> Unit = { Text(it.name) },
) {
    val values = remember { enumValues<T>() }
    RainbowDropdownMenuHolder(
        text = { Text(text = value.name) },
        icon = icon,
        containerColor = containerColor,
        enabled = enabled,
        modifier = modifier,
    ) { handler ->
        values.forEach {
            RainbowDropdownMenuItem(
                selected = it == value,
                onClick = {
                    onValueUpdate(it)
                    handler.hideMenu()
                },
            ) {
                content(it)
            }
        }
    }
}

@Composable
fun RainbowDropdownMenuHolder(
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.background,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.(DropdownMenuHandler) -> Unit,
) {
    var isVisible by remember { mutableStateOf(false) }
    val handler = remember {
        object : DropdownMenuHandler {
            override fun hideMenu() {
                isVisible = false
            }
        }
    }
    Column(modifier) {
        RainbowIconButton(
            onClick = {
                isVisible = !isVisible
                onClick?.invoke()
            },
            containerColor = containerColor,
            content = icon,
        )

        RainbowDropdownMenu(
            isVisible = isVisible,
            onDismissRequest = { isVisible = false },
        ) {
            content(handler)
        }
    }
}

@Composable
fun RainbowDropdownMenuHolder(
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.background,
    icon: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.(DropdownMenuHandler) -> Unit,
) {
    var isVisible by remember { mutableStateOf(false) }
    val iconRotation by animateFloatAsState(if (isVisible) 180F else 0F)
    val handler = remember {
        object : DropdownMenuHandler {
            override fun hideMenu() {
                isVisible = false
            }
        }
    }
    Column(modifier) {
        RainbowButton(
            onClick = {
                isVisible = !isVisible
                onClick?.invoke()
            },
            modifier = modifier,
            enabled = enabled,
            containerColor = containerColor,
        ) {
            if (icon != null) icon()
            text()
            Icon(RainbowIcons.ExpandMore, RainbowStrings.Show, Modifier.rotate(iconRotation))
        }

        RainbowDropdownMenu(
            isVisible = isVisible,
            onDismissRequest = { isVisible = false },
        ) {
            content(handler)
        }
    }
}

@Composable
fun RainbowDropdownMenuItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
//            .background(MaterialTheme.colorScheme.surface)
//            .padding(horizontal = RainbowTheme.dpDimensions.medium)
//            .clip(MaterialTheme.shapes.small)
            .clickable(onClick = onClick, enabled = enabled)
            .background(MaterialTheme.colorScheme.surface)
            .padding(RainbowTheme.dimensions.medium),
        horizontalArrangement = Arrangement.spacedBy(RainbowTheme.dimensions.medium),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
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
//            .background(MaterialTheme.colorScheme.surface)
//            .padding(horizontal = RainbowTheme.dpDimensions.medium)
//            .clip(MaterialTheme.shapes.small)
            .selectable(selected, enabled, onClick = onClick)
            .background(if (selected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surface)
            .padding(RainbowTheme.dimensions.medium),
        horizontalArrangement = Arrangement.spacedBy(RainbowTheme.dimensions.medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
        if (selected) {
            Spacer(Modifier.weight(1F))
            Icon(RainbowIcons.CheckCircleOutline, contentDescription = null)
        }
    }
}

interface DropdownMenuHandler {
    fun hideMenu()
}

@Composable
fun RainbowDropdownMenu(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    // TODO: Remove vertical padding when it's possible.
    DropdownMenu(
        isVisible,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .sizeIn(minWidth = DropDownMenuMinWidth, maxWidth = DropdownMenuMaxWidth, maxHeight = DropdownMenuMaxHeight)
            .background(MaterialTheme.colorScheme.surface),
        offset = DropdownMenuOffset,
        content = content,
    )
}