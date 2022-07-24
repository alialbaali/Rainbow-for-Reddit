package com.rainbow.desktop.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun RainbowMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    content: @Composable ColumnScope.() -> Unit = { },
) {
    DropdownMenu(
        expanded,
        onDismissRequest,
        modifier = modifier,
        offset = offset,
        content = content,
    )
}

@Composable
fun RainbowMenuItem(
    text: String,
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    DropdownMenuItem(onClick, modifier, enabled) {
        Icon(imageVector, contentDescription = text)
        Spacer(Modifier.width(16.dp))
        Text(text)
    }
}