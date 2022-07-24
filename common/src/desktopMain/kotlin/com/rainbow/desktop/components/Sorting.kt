package com.rainbow.desktop.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.domain.models.PostSorting
import com.rainbow.domain.models.TimeSorting

@Composable
inline fun <reified T : Enum<T>> Sorting(
    postsSorting: T,
    crossinline onSortingUpdate: (T) -> Unit,
    timeSorting: TimeSorting,
    crossinline onTimeSortingUpdate: (TimeSorting) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        DropdownMenuHolder(
            postsSorting,
            onSortingUpdate
        )

        if (postsSorting is PostSorting && postsSorting.isTimeSorting)
            DropdownMenuHolder(
                timeSorting,
                onTimeSortingUpdate
            )
    }
}