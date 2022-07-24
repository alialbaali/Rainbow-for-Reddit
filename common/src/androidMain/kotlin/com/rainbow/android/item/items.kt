package com.rainbow.android.item

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import com.rainbow.android.comment.CommentItem
import com.rainbow.common.components.RainbowProgressIndicator
import com.rainbow.android.post.PostItem
import com.rainbow.common.utils.UIState
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Item
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.PostLayout

inline fun LazyListScope.items(
    itemsState: UIState<List<Item>>,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    crossinline onPostClick: (Post) -> Unit,
    crossinline onCommentClick: (Comment) -> Unit,
    noinline onPostUpdate: (Post) -> Unit,
    noinline onCommentUpdate: (Comment) -> Unit,
    crossinline onPostMenuClick: () -> Unit,
    crossinline onShowAwards: (Post) -> Unit,
) {
    when (itemsState) {
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
                is Post -> PostItem(
                    item,
                    onUserNameClick,
                    onSubredditNameClick,
                    onPostClick,
                    onPostUpdate,
                    onPostMenuClick,
                    onShowAwards,
                    Modifier.fillParentMaxWidth()
                )
            }
        }
    }
}