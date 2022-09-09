package com.rainbow.desktop.comment

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.utils.PagingEffect
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.getOrDefault
import com.rainbow.domain.models.Comment

fun LazyListScope.comments(
    state: UIState<List<Comment>>,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    onLoadMore: (Comment) -> Unit,
) {
    val comments = state.getOrDefault(emptyList())
    itemsIndexed(comments) { index, comment ->
        CommentItem(
            comment,
            onNavigateMainScreen,
            onNavigateDetailsScreen,
            modifier = Modifier.fillParentMaxWidth()
        )
        PagingEffect(comments, index, onLoadMore)
    }
    if (state.isLoading) {
        item {
            RainbowProgressIndicator()
        }
    }
}