package com.rainbow.desktop.post

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.utils.PagingEffect
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.getOrDefault
import com.rainbow.domain.models.Post

inline fun LazyListScope.posts(
    state: UIState<List<Post>>,
    crossinline onNavigateMainScreen: (MainScreen) -> Unit,
    crossinline onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    crossinline onAwardsClick: () -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    crossinline onLoadMore: (Post) -> Unit = {},
) {
    val posts = state.getOrDefault(emptyList())
    itemsIndexed(posts) { index, post ->
        PostItem(
            post,
            onNavigateMainScreen,
            onNavigateDetailsScreen,
            onAwardsClick,
            onShowSnackbar,
            Modifier.fillParentMaxWidth(),
        )
        PagingEffect(posts, index, onLoadMore)
    }
    if (state.isLoading) {
        item {
            RainbowProgressIndicator()
        }
    }
}