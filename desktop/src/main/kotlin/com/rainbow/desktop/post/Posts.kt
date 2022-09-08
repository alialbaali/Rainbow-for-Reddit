package com.rainbow.desktop.post

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.utils.PagingEffect
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.getOrDefault
import com.rainbow.domain.models.Post

fun LazyListScope.posts(
    state: UIState<List<Post>>,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    onAwardsClick: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    onLoadMore: (Post) -> Unit,
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

fun LazyGridScope.posts(
    state: UIState<List<Post>>,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    onAwardsClick: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    onLoadMore: (Post) -> Unit,
) {
    val posts = state.getOrDefault(emptyList())
    itemsIndexed(posts) { index, post ->
        PostItem(
            post,
            onNavigateMainScreen,
            onNavigateDetailsScreen,
            onAwardsClick,
            onShowSnackbar,
            Modifier.fillMaxWidth(),
        )
        PagingEffect(posts, index, onLoadMore)
    }
    if (state.isLoading) {
        item {
            RainbowProgressIndicator()
        }
    }
}