package com.rainbow.app.item

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import com.rainbow.app.comment.CommentItem
import com.rainbow.app.components.RainbowProgressIndicator
import com.rainbow.app.post.CompactPostItem
import com.rainbow.app.post.PostItem
import com.rainbow.app.utils.UIState
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Item
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.PostLayout

inline fun LazyListScope.items(
    itemsState: UIState<List<Item>>,
    postLayout: PostLayout,
    focusRequester: FocusRequester,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    crossinline onPostClick: (Post) -> Unit,
    crossinline onCommentClick: (Comment) -> Unit,
    noinline onPostUpdate: (Post) -> Unit,
    noinline onCommentUpdate: (Comment) -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
) {
    when (itemsState) {
        is UIState.Empty -> item { Text("Empty") }
        is UIState.Failure -> item { Text("Failed loading items") }
        is UIState.Loading -> item { RainbowProgressIndicator() }
        is UIState.Success -> items(itemsState.value) { item ->
            when (item) {
                is Comment -> CommentItem(
                    item,
                    onUserNameClick,
                    onSubredditNameClick,
                    onCommentClick,
                    onCommentUpdate,
                    Modifier.fillParentMaxWidth()
                )
                is Post -> when (postLayout) {
                    PostLayout.Card -> PostItem(
                        item,
                        onPostUpdate,
                        focusRequester,
                        onPostClick,
                        onUserNameClick,
                        onSubredditNameClick,
                        onShowSnackbar,
                        Modifier.fillParentMaxWidth()
                    )
                    PostLayout.Compact -> CompactPostItem(
                        item,
                        onPostUpdate,
                        focusRequester,
                        onPostClick,
                        onUserNameClick,
                        onSubredditNameClick,
                        onShowSnackbar,
                        Modifier.fillParentMaxWidth()
                    )
                }
            }
        }
    }
}