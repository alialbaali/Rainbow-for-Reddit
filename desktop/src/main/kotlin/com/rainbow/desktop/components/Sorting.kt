package com.rainbow.desktop.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.rainbow.desktop.ui.dimensions
import com.rainbow.desktop.utils.icon
import com.rainbow.domain.models.ItemSorting
import com.rainbow.domain.models.PostSorting
import com.rainbow.domain.models.TimeSorting

@Composable
inline fun <reified T> PostSorting(
    postSorting: T,
    timeSorting: TimeSorting,
    crossinline onSortingUpdate: (T) -> Unit,
    noinline onTimeSortingUpdate: (TimeSorting) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
) where T : Enum<T>, T : PostSorting {
    Row(
        modifier,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ItemSorting(
            postSorting,
            onSortingUpdate,
            containerColor = containerColor
        )
        TimeSorting(
            timeSorting,
            onTimeSortingUpdate,
            enabled = postSorting.isTimeSorting,
            containerColor = containerColor
        )
    }
}

@Composable
inline fun <reified T> ItemSorting(
    itemSorting: T,
    crossinline onSortingUpdate: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.surface,
) where T : Enum<T>, T : ItemSorting {
    EnumDropdownMenuHolder(
        value = itemSorting,
        onValueUpdate = onSortingUpdate,
        icon = { Icon(itemSorting.icon, itemSorting.name) },
        modifier = modifier,
        enabled = enabled,
        containerColor = containerColor,
    ) {
        Icon(it.icon, it.name)
        Text(text = it.name)
    }
}

@Composable
fun TimeSorting(
    timeSorting: TimeSorting,
    onTimeSortingUpdate: (TimeSorting) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.surface,
) {
    EnumDropdownMenuHolder(
        value = timeSorting,
        onValueUpdate = onTimeSortingUpdate,
        modifier = modifier,
        enabled = enabled,
        containerColor = containerColor,
    ) {
        Text(text = it.name)
    }
}