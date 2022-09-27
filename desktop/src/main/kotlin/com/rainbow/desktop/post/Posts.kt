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
import com.rainbow.domain.models.PostLayout

fun LazyListScope.posts(
    state: UIState<List<Post>>,
    postLayout: PostLayout,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    onLoadMore: (Post) -> Unit,
) {
    val posts = state.getOrDefault(emptyList())
    val onPostClick = { postId: String ->
        onNavigateDetailsScreen(DetailsScreen.Post(postId))
    }
    val onUserNameClick = { userName: String ->
        onNavigateMainScreen(MainScreen.User(userName))
    }

    val onSubredditNameClick = { subredditName: String ->
        onNavigateMainScreen(MainScreen.Subreddit(subredditName))
    }

    itemsIndexed(posts) { index, post ->
        when (postLayout) {
            PostLayout.Compact -> CompactPostItem(
                post,
                onClick = { onPostClick(post.id) },
                onUserNameClick,
                onSubredditNameClick,
                onShowSnackbar,
                Modifier.fillParentMaxWidth(),
            )

            PostLayout.Card -> CardPostItem(
                post,
                onClick = { onPostClick(post.id) },
                onUserNameClick,
                onSubredditNameClick,
                onShowSnackbar,
                Modifier.fillParentMaxWidth(),
            )

            PostLayout.Large -> LargePostItem(
                post,
                onClick = { onPostClick(post.id) },
                onUserNameClick,
                onSubredditNameClick,
                onShowSnackbar,
                Modifier.fillParentMaxWidth(),
            )
        }

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
    postLayout: PostLayout,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    onLoadMore: (Post) -> Unit,
) {
    val posts = state.getOrDefault(emptyList())
    val onPostClick = { postId: String ->
        onNavigateDetailsScreen(DetailsScreen.Post(postId))
    }
    val onUserNameClick = { userName: String ->
        onNavigateMainScreen(MainScreen.User(userName))
    }

    val onSubredditNameClick = { subredditName: String ->
        onNavigateMainScreen(MainScreen.Subreddit(subredditName))
    }

    itemsIndexed(posts) { index, post ->
        when (postLayout) {
            PostLayout.Compact -> CompactPostItem(
                post,
                onClick = { onPostClick(post.id) },
                onUserNameClick,
                onSubredditNameClick,
                onShowSnackbar,
                Modifier.fillMaxWidth(),
            )

            PostLayout.Card -> CardPostItem(
                post,
                onClick = { onPostClick(post.id) },
                onUserNameClick,
                onSubredditNameClick,
                onShowSnackbar,
                Modifier.fillMaxWidth(),
            )

            PostLayout.Large -> LargePostItem(
                post,
                onClick = { onPostClick(post.id) },
                onUserNameClick,
                onSubredditNameClick,
                onShowSnackbar,
                Modifier.fillMaxWidth(),
            )
        }

        PagingEffect(posts, index, onLoadMore)
    }

    if (state.isLoading) {
        item {
            RainbowProgressIndicator()
        }
    }
}