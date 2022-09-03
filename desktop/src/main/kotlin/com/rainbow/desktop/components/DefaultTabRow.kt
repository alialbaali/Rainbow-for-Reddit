package com.rainbow.desktop.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

@Composable
inline fun <reified T : Enum<T>> ScrollableEnumTabRow(
    selectedTab: T,
    crossinline onTabClick: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    val values = enumValues<T>()
    ScrollableTabRow(
        values.indexOf(selectedTab),
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier.clip(MaterialTheme.shapes.medium),
        divider = {},
        contentColor = MaterialTheme.colorScheme.primary,
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

@Composable
inline fun <reified T : Enum<T>> EnumTabRow(
    selectedTab: T,
    crossinline onTabClick: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    val values = enumValues<T>()
    TabRow(
        values.indexOf(selectedTab),
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier.clip(MaterialTheme.shapes.medium),
        divider = {},
        contentColor = MaterialTheme.colorScheme.primary,
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
