package com.rainbow.desktop.components

import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.domain.models.PostSorting

@Suppress("UNCHECKED_CAST")
@Composable
fun <T : PostSorting> PostSortingItem(
    currentSorting: T,
    onSortingUpdate: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    val values = remember(currentSorting) { PostSorting.valuesOf(currentSorting) }
    RainbowMenu(modifier) {
        values.forEach { sorting ->
            RainbowMenuItem(
                text = sorting.name,
                imageVector = RainbowIcons.Star,
                onClick = { onSortingUpdate(sorting as T) },
            )
        }
    }
}