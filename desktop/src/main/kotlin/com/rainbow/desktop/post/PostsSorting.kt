package com.rainbow.desktop.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.DropdownMenuHolder
import com.rainbow.desktop.ui.dpDimensions
import com.rainbow.domain.models.PostSorting
import com.rainbow.domain.models.TimeSorting

@Composable
inline fun <reified T : Enum<T>> SortingItem(
    postSorting: T,
    timeSorting: TimeSorting,
    crossinline setPostSorting: (T) -> Unit,
    crossinline setTimeSorting: (TimeSorting) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dpDimensions.medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DropdownMenuHolder(
            value = postSorting,
            onValueUpdate = setPostSorting,
        )

        if (postSorting is PostSorting) {
            DropdownMenuHolder(
                value = timeSorting,
                onValueUpdate = setTimeSorting,
                enabled = postSorting.isTimeSorting,
            )
        }
    }
}