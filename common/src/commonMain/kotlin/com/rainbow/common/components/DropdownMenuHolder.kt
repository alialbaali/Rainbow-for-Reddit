package com.rainbow.common.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
//import androidx.compose.material.DropdownMenu
//import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.rainbow.common.utils.RainbowIcons
import com.rainbow.domain.models.Sorting

@Composable
inline fun <reified T : Enum<T>> DropdownMenuHolder(
    value: T,
    crossinline onValueUpdate: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isMenuVisible by remember { mutableStateOf(false) }
    val iconRotation by animateFloatAsState(if (isMenuVisible) 180F else 0F)
    val values = remember { enumValues<T>() }
    Column(modifier) {
        TextIconButton(
            value.name,
            RainbowIcons.ArrowDropUp,
            value.name,
            onClick = { isMenuVisible = !isMenuVisible },
            iconModifier = Modifier.rotate(iconRotation)
        )

//        DropdownMenu(
//            isMenuVisible,
//            onDismissRequest = { isMenuVisible = !isMenuVisible },
//        ) {
//            values.forEach { value ->
//                DropdownMenuItem(
//                    onClick = {
//                        onValueUpdate(value)
//                        isMenuVisible = false
//                    },
//                    Modifier
//                        .fillMaxWidth()
//                        .wrapContentHeight()
//                ) {
//                    (value as? Sorting)?.icon?.let { icon ->
//                        Icon(icon, value.name)
//                    }
//                    Spacer(Modifier.width(16.dp))
//                    Text(text = value.name, Modifier.fillMaxWidth())
//                }
//            }
//        }
    }
}