package com.rainbow.app.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rainbow.app.utils.defaultSurfaceShape

@Composable
inline fun <reified T : Enum<T>> DefaultTabRow(
    selectedTab: T,
    crossinline onTabClick: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    val values = enumValues<T>()
    ScrollableTabRow(
        values.indexOf(selectedTab),
        backgroundColor = MaterialTheme.colors.surface,
        modifier = modifier.defaultSurfaceShape(),
        divider = {},
        contentColor = MaterialTheme.colors.primary,
    ) {
        values.forEach { tab ->
            Tab(
                selected = tab == selectedTab,
                onClick = { onTabClick(tab) },
                text = {
                    Text(tab.name)
                }
            )
        }
    }
}