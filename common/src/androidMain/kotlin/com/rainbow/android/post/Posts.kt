package com.rainbow.android.post

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import com.rainbow.common.components.RainbowProgressIndicator
import com.rainbow.common.utils.UIState
import com.rainbow.domain.models.Post

inline fun LazyListScope.posts(
    postsState: UIState<List<Post>>,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    crossinline onPostClick: (Post) -> Unit,
    noinline onPostUpdate: (Post) -> Unit,
    crossinline onPostMenuClick: () -> Unit,
    crossinline onShowAwards: (Post) -> Unit,
) {
    when (postsState) {
        is UIState.Failure -> item { Text("Failed loading posts") }
        is UIState.Loading -> item { RainbowProgressIndicator() }
        is UIState.Success -> {
            items(postsState.value) { post ->
                PostItem(
                    post,
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