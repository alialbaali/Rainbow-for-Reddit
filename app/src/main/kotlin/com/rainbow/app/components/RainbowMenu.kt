package com.rainbow.app.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    content: @Composable ColumnScope.() -> Unit = {  },
) {
    DropdownMenu(
        expanded,
        onDismissRequest,
        modifier,
        offset,
        content,
    )
}

@Composable
fun RainbowMenuItem(
    text: String,
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onclick: () -> Unit,
) {
    DropdownMenuItem(onclick, modifier, enabled) {
        Row(
            Modifier
                .fillMaxSize(),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically,
        ) {
            Icon(imageVector, contentDescription = text)
            Text(text)
        }
    }
}