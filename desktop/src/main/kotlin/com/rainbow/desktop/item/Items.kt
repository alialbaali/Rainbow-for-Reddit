package com.rainbow.desktop.item

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Modifier
import com.rainbow.desktop.comment.CommentItem
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.post.PostItem
import com.rainbow.desktop.utils.PagingEffect
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.getOrDefault
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Item
import com.rainbow.domain.models.Post

inline fun LazyListScope.items(
    itemsState: UIState<List<Item>>,
    crossinline onNavigateMainScreen: (MainScreen) -> Unit,
    crossinline onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    crossinline onAwardsClick: () -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    crossinline onLoadMore: (Item) -> Unit,
) {
    val items = itemsState.getOrDefault(emptyList())
    itemsIndexed(items) { index, item ->
        when (item) {
            is Comment -> CommentItem(
                item,
                onNavigateMainScreen,
                onNavigateDetailsScreen,
                Modifier.fillParentMaxWidth()
            )

            is Post -> PostItem(
                item,
                onNavigateMainScreen,
                onNavigateDetailsScreen,
                onAwardsClick,
                onShowSnackbar,
                Modifier.fillParentMaxWidth()
            )
        }
        PagingEffect(items, index, onLoadMore)
    }
    if (itemsState.isLoading) {
        item {
            RainbowProgressIndicator()
        }
    }
}