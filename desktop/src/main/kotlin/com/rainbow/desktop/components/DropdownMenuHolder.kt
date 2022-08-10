package com.rainbow.desktop.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.icon
import com.rainbow.domain.models.Sorting

@Composable
inline fun <reified T : Enum<T>> DropdownMenuHolder(
    value: T,
    crossinline onValueUpdate: (T) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    var isMenuVisible by remember { mutableStateOf(false) }
    val values = remember { enumValues<T>() }
    Column(modifier) {
        SelectionButton(
            isMenuVisible,
            value.name,
            RainbowIcons.ExpandLess,
            onClick = { isMenuVisible = !isMenuVisible },
            enabled = enabled,
        )

        DropdownMenu(
            isMenuVisible,
            onDismissRequest = { isMenuVisible = false },
            modifier = Modifier.clip(MaterialTheme.shapes.medium),
            offset = DpOffset(0.dp, 16.dp)
        ) {
            values.forEach { value ->
                DropdownMenuItem(
                    onClick = {
                        onValueUpdate(value)
                        isMenuVisible = false
                    },
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    (value as? Sorting)?.icon?.let { icon ->
                        Icon(icon, value.name)
                    }
                    Spacer(Modifier.width(16.dp))
                    Text(text = value.name, Modifier.fillMaxWidth())
                }
            }
        }
    }
}