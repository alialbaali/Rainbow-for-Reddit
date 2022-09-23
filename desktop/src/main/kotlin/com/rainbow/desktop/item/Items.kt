package com.rainbow.desktop.item

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Modifier
import com.rainbow.desktop.comment.CommentItem
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.post.CardPostItem
import com.rainbow.desktop.post.CompactPostItem
import com.rainbow.desktop.post.LargePostItem
import com.rainbow.desktop.utils.PagingEffect
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.getOrDefault
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Item
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.PostLayout

fun LazyListScope.items(
    itemsState: UIState<List<Item>>,
    postLayout: PostLayout,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    onAwardsClick: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    onLoadMore: (Item) -> Unit,
) {
    val items = itemsState.getOrDefault(emptyList())
    itemsIndexed(items) { index, item ->
        when (item) {
            is Comment -> CommentItem(
                item,
                onNavigateMainScreen,
                onNavigateDetailsScreen,
                onShowSnackbar,
                Modifier.fillParentMaxWidth()
            )

            is Post -> when (postLayout) {
                PostLayout.Compact -> CompactPostItem(
                    item,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen,
                    onAwardsClick,
                    onShowSnackbar,
                    Modifier.fillParentMaxWidth(),
                )

                PostLayout.Card -> CardPostItem(
                    item,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen,
                    onAwardsClick,
                    onShowSnackbar,
                    Modifier.fillParentMaxWidth(),
                )

                PostLayout.Large -> LargePostItem(
                    item,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen,
                    onAwardsClick,
                    onShowSnackbar,
                    Modifier.fillParentMaxWidth()
                )
            }
        }
        PagingEffect(items, index, onLoadMore)
    }
    if (itemsState.isLoading) {
        item {
            RainbowProgressIndicator()
        }
    }
}