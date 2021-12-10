package com.rainbow.app.post

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.ui.focus.FocusRequester
import com.rainbow.app.components.RainbowProgressIndicator
import com.rainbow.app.utils.PagingEffect
import com.rainbow.app.utils.UIState
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.PostLayout

inline fun LazyListScope.posts(
    postsState: UIState<List<Post>>,
    noinline onPostUpdate: (Post) -> Unit,
    postLayout: PostLayout,
    focusRequester: FocusRequester,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    crossinline setLastPost: (Post) -> Unit,
    crossinline onPostClick: (Post) -> Unit,
) {
    when (postsState) {
        is UIState.Empty -> item { Text("No posts found") }
        is UIState.Failure -> item { Text("Failed loading posts") }
        is UIState.Loading -> item { RainbowProgressIndicator() }
        is UIState.Success -> {
            val posts = postsState.value
            when (postLayout) {
                PostLayout.Card -> itemsIndexed(posts) { index, post ->
                    PostItem(
                        post,
                        onPostUpdate,
                        focusRequester,
                        onPostClick,
                        onUserNameClick,
                        onSubredditNameClick,
                        onShowSnackbar,
                    )
                    PagingEffect(posts, index, setLastPost)
                }
                PostLayout.Compact -> itemsIndexed(posts) { index, post ->
                    CompactPostItem(
                        post,
                        onPostUpdate,
                        focusRequester,
                        onPostClick,
                        onUserNameClick,
                        onSubredditNameClick,
                        onShowSnackbar,
                    )
                    PagingEffect(posts, index, setLastPost)
                }
            }
        }
    }
}