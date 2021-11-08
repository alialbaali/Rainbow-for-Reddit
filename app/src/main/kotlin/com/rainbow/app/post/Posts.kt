package com.rainbow.app.post

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import com.rainbow.app.PagingEffect
import com.rainbow.app.components.RainbowProgressIndicator
import com.rainbow.app.utils.OneTimeEffect
import com.rainbow.app.utils.UIState
import com.rainbow.domain.models.Post

inline fun LazyListScope.posts(
    postsState: UIState<List<Post>>,
    crossinline onPostClick: (Post) -> Unit,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
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
                    onPostClick(posts.first())
                }
            }
            itemsIndexed(posts) { index, post ->
                PostItem(
                    post,
                    onPostClick,
                    onUserNameClick,
                    onSubredditNameClick
                )
                PagingEffect(posts, index, onLoadMore)
            }
        }
    }
}