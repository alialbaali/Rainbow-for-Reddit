package com.rainbow.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.common.components.icon
import com.rainbow.domain.models.Sorting

@Composable
inline fun <reified T : Enum<T>> SortingBottomSheet(
    crossinline onPostSortingUpdate: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    val values = remember { enumValues<T>() }
    Column(modifier) {
        values.forEach { sorting ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { onPostSortingUpdate(sorting) }
                    .padding(16.dp),
                Arrangement.spacedBy(16.dp),
                Alignment.CenterVertically
            ) {
                (sorting as? Sorting)?.let { sorting ->
                    Icon(sorting.icon, sorting.name)
                }
                Text(sorting.name)
            }
        }
    }
}