package com.rainbow.desktop.components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.ui.DefaultScrollbarStyle

@Composable
fun RainbowLazyColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(16.dp),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: LazyListScope.() -> Unit,
) {
    val scrollingState = rememberLazyListState()
    Box(modifier) {
        LazyColumn(
            state = scrollingState,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            content = content
        )
        VerticalScrollbar(
            rememberScrollbarAdapter(scrollingState),
            Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd),
            style = DefaultScrollbarStyle
        )
    }
}