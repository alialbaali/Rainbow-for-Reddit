package com.rainbow.desktop.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
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
    containerColor: Color = MaterialTheme.colorScheme.surface,
    modifier: Modifier = Modifier,
) {
    var isMenuVisible by remember { mutableStateOf(false) }
    val values = remember { enumValues<T>() }
    val iconRotation by animateFloatAsState(if (isMenuVisible) 180F else 0F)
    Column(modifier) {
        RainbowButton(
            onClick = { isMenuVisible = !isMenuVisible },
            modifier = modifier,
            enabled = enabled,
            containerColor = containerColor,
        ) {
            if (value is Sorting) {
                Icon(value.icon, contentDescription = value.name)
            }
            Text(text = value.name)
            Icon(RainbowIcons.ExpandMore, contentDescription = value.name, Modifier.rotate(iconRotation))
        }

        val containerColor = MaterialTheme.colorScheme.background

        DropdownMenu(
            isMenuVisible,
            onDismissRequest = { isMenuVisible = false },
            modifier = Modifier.background(containerColor),
            offset = DpOffset(0.dp, 16.dp)
        ) {
            values.forEach { value ->
                Surface(color = containerColor) {
                    DropdownMenuItem(
                        onClick = {
                            onValueUpdate(value)
                            isMenuVisible = false
                        },
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        if (value is Sorting) {
                            Icon(value.icon, value.name)
                        }
                        Spacer(Modifier.width(16.dp))
                        Text(
                            text = value.name,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}