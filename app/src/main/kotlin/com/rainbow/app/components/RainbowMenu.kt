package com.rainbow.app.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.emptyContent
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.rainbow.app.utils.RainbowIcons

@Composable
fun RainbowMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onToggleClick: () -> Unit,
    menuModifier: Modifier = Modifier,
    toggleModifier: Modifier = Modifier,
    dropdownOffset: DpOffset = DpOffset(0.dp, 0.dp),
    content: @Composable ColumnScope.() -> Unit = { emptyContent() },
) {
    DropdownMenu(
        toggle = {
            IconButton(onToggleClick) {
                Icon(RainbowIcons.MoreVert)
            }
        },
        expanded,
        onDismissRequest,
        toggleModifier,
        dropdownOffset,
        menuModifier,
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
            Icon(imageVector)
            Text(text)
        }
    }
}