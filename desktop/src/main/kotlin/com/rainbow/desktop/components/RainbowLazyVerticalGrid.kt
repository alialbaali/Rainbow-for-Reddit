package com.rainbow.desktop.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rainbow.desktop.ui.dimensions
import com.rainbow.desktop.utils.DefaultContentPadding

private const val ColumnsCount = 4

@Composable
fun RainbowLazyVerticalGrid(
    modifier: Modifier = Modifier,
    columns: GridCells = GridCells.Fixed(ColumnsCount),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(MaterialTheme.dimensions.medium),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(MaterialTheme.dimensions.medium),
    content: LazyGridScope.() -> Unit,
) {
    LazyVerticalGrid(
        columns = columns,
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement,
        contentPadding = DefaultContentPadding(),
        content = content,
    )
}