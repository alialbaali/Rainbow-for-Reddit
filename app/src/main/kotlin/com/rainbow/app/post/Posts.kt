package com.rainbow.app.post

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.ui.focus.FocusRequester
import com.rainbow.app.components.RainbowProgressIndicator
import com.rainbow.app.utils.OneTimeEffect
import com.rainbow.app.utils.PagingEffect
import com.rainbow.app.utils.UIState
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.PostLayout

inline fun LazyListScope.posts(
    postsState: UIState<List<Post>>,
    postLayout: PostLayout,
    focusRequester: FocusRequester,
    crossinline onPostClick: (Post) -> Unit,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    crossinline onLoadMore: (Post) -> Unit,
) {
    when (postsState) {
        is UIState.Empty -> item { Text("No posts found") }
        is UIState.Failure -> item { Text("Failed loading posts") }
        is UIState.Loading -> item { RainbowProgressIndicator() }
        is UIState.Success -> {
            val posts = postsState.value
            item {
                OneTimeEffect(posts) {
                    posts.firstOrNull()?.let { onPostClick(it) }
                }
            }
            itemsIndexed(posts) { index, post ->
                when (postLayout) {
                    PostLayout.Card -> PostItem(
                        post,
                        focusRequester,
                        onPostClick,
                        onUserNameClick,
                        onSubredditNameClick,
                        onShowSnackbar,
                    )
                    PostLayout.Compact -> CompactPostItem(
                        post,
                        focusRequester,
                        onPostClick,
                        onUserNameClick,
                        onSubredditNameClick,
                        onShowSnackbar,
                    )
                }
                PagingEffect(posts, index, onLoadMore)
            }
        }
    }
}