package com.rainbow.desktop.post

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.navigation.ContentScreen
import com.rainbow.desktop.navigation.Screen
import com.rainbow.desktop.utils.UIState
import com.rainbow.domain.models.Post

inline fun LazyListScope.posts(
    state: UIState<List<Post>>,
    crossinline onNavigate: (Screen) -> Unit,
    crossinline onNavigateContentScreen: (ContentScreen) -> Unit,
    noinline onPostUpdate: (Post) -> Unit,
    crossinline onAwardsClick: () -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
) {
    when (state) {
        is UIState.Failure -> item { Text("Failed loading posts") }
        is UIState.Loading -> item { RainbowProgressIndicator() }
        is UIState.Success -> {
            val posts = state.value
            items(posts) { post ->
                PostItem(
                    post,
                    onNavigate,
                    onNavigateContentScreen,
                    onPostUpdate,
                    onAwardsClick,
                    onShowSnackbar,
                    Modifier.fillParentMaxWidth(),
                )
            }
        }

        UIState.Empty -> {}
    }
}