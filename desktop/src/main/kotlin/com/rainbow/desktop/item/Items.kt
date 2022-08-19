package com.rainbow.desktop.item

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.rainbow.desktop.comment.CommentItem
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.post.PostItem
import com.rainbow.desktop.utils.UIState
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Item
import com.rainbow.domain.models.Post

inline fun LazyListScope.items(
    itemsState: UIState<List<Item>>,
    crossinline onNavigateMainScreen: (MainScreen) -> Unit,
    crossinline onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    crossinline onAwardsClick: () -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
) {
    when (itemsState) {
        is UIState.Failure -> item { Text("Failed loading items") }
        is UIState.Loading -> item { RainbowProgressIndicator() }
        is UIState.Success -> items(itemsState.data) { item ->
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
        }

        is UIState.Empty -> {}
    }
}