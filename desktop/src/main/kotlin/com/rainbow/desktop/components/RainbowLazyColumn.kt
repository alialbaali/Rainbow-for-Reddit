package com.rainbow.desktop.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.DefaultContentPadding

@Composable
fun RainbowLazyColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(RainbowTheme.dpDimensions.medium),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    scrollingState: LazyListState = rememberLazyListState(),
    content: LazyListScope.() -> Unit,
) {
    Box(modifier) {
        LazyColumn(
            state = scrollingState,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            content = content,
            contentPadding = DefaultContentPadding(),
        )
//        VerticalScrollbar(
//            rememberScrollbarAdapter(scrollingState),
//            Modifier
//                .fillMaxHeight()
//                .align(Alignment.CenterEnd),
//            style = DefaultScrollbarStyle
//        )
    }
}