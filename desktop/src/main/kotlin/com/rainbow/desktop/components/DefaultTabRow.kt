package com.rainbow.desktop.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

@Composable
inline fun <reified T : Enum<T>> ScrollableEnumTabRow(
    selectedTab: T,
    crossinline onTabClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    noinline icon: (@Composable (T) -> Unit)? = null,
    crossinline text: @Composable (T) -> Unit = { Text(it.name) },
) {
    val values = remember { enumValues<T>() }
    val selectedTabIndex = remember(values, selectedTab) { values.indexOf(selectedTab) }
    RainbowScrollableTabRow(selectedTabIndex, modifier) {
        values.forEach { tab ->
            RainbowTab(
                selected = tab == selectedTab,
                onClick = { onTabClick(tab) },
                text = { text(tab) },
                icon = if (icon != null) {
                    { icon(tab) }
                } else
                    null,
            )
        }
    }
}

@Composable
inline fun <reified T : Enum<T>> EnumTabRow(
    selectedTab: T,
    crossinline onTabClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    noinline icon: (@Composable (T) -> Unit)? = null,
    crossinline text: @Composable (T) -> Unit = { Text(it.name) },
) {
    val values = remember { enumValues<T>() }
    val selectedTabIndex = remember(values, selectedTab) { values.indexOf(selectedTab) }
    RainbowTabRow(selectedTabIndex, modifier) {
        values.forEach { tab ->
            RainbowTab(
                selected = tab == selectedTab,
                onClick = { onTabClick(tab) },
                text = { text(tab) },
                icon = if (icon != null) {
                    { icon(tab) }
                } else
                    null,
            )
        }
    }
}

@Composable
fun RainbowScrollableTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    ScrollableTabRow(
        selectedTabIndex,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier.clip(MaterialTheme.shapes.small),
        divider = {},
        contentColor = MaterialTheme.colorScheme.primary,
        tabs = content,
    )
}

@Composable
fun RainbowTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    TabRow(
        selectedTabIndex,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier.clip(MaterialTheme.shapes.small),
        divider = {},
        contentColor = MaterialTheme.colorScheme.primary,
        tabs = content,
    )
}

@Composable
fun RainbowTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
) {
    Tab(
        selected,
        onClick,
        modifier,
        enabled,
        text,
        icon,
        unselectedContentColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}