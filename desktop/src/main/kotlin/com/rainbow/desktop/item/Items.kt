package com.rainbow.desktop.item

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.rainbow.desktop.comment.CommentItem
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.navigation.ContentScreen
import com.rainbow.desktop.navigation.Screen
import com.rainbow.desktop.post.PostItem
import com.rainbow.desktop.utils.UIState
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Item
import com.rainbow.domain.models.Post

inline fun LazyListScope.items(
    itemsState: UIState<List<Item>>,
    crossinline onNavigate: (Screen) -> Unit,
    crossinline onNavigateContentScreen: (ContentScreen) -> Unit,
    noinline onPostUpdate: (Post) -> Unit,
    noinline onCommentUpdate: (Comment) -> Unit,
    crossinline onAwardsClick: () -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
) {
    when (itemsState) {
        is UIState.Failure -> item { Text("Failed loading items") }
        is UIState.Loading -> item { RainbowProgressIndicator() }
        is UIState.Success -> items(itemsState.value) { item ->
            when (item) {
                is Comment -> CommentItem(
                    item,
                    onNavigate,
                    onNavigateContentScreen,
                    onCommentUpdate,
                    Modifier.fillParentMaxWidth()
                )

                is Post -> PostItem(
                    item,
                    onNavigate,
                    onNavigateContentScreen,
                    onPostUpdate,
                    onAwardsClick,
                    onShowSnackbar,
                    Modifier.fillParentMaxWidth()
                )
            }
        }

        UIState.Empty -> {}
    }
}