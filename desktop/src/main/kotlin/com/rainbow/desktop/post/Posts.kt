package com.rainbow.desktop.post

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.utils.PagingEffect
import com.rainbow.desktop.utils.UIState
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.PostLayout

inline fun LazyListScope.posts(
    postsState: UIState<List<Post>>,
    postLayout: PostLayout,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    crossinline onPostClick: (Post) -> Unit,
    noinline onPostUpdate: (Post) -> Unit,
    crossinline onAwardsClick: () -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    crossinline setLastPost: (Post) -> Unit,
) {
    when (postsState) {
        is UIState.Failure -> item { Text("Failed loading posts") }
        is UIState.Loading -> item { RainbowProgressIndicator() }
        is UIState.Success -> {
            val posts = postsState.value
            when (postLayout) {
                PostLayout.Card -> itemsIndexed(posts) { index, post ->
                    PostItem(
                        post,
                        onUserNameClick,
                        onSubredditNameClick,
                        onPostClick,
                        onPostUpdate,
                        onAwardsClick,
                        onShowSnackbar,
                    )
                    PagingEffect(posts, index, setLastPost)
                }

                PostLayout.Compact -> itemsIndexed(posts) { index, post ->
                    CompactPostItem(
                        post,
                        onPostUpdate,
                        onPostClick,
                        onUserNameClick,
                        onSubredditNameClick,
                        onShowSnackbar,
                    )
                    PagingEffect(posts, index, setLastPost)
                }
            }
        }

        UIState.Empty -> {}
    }
}