package com.rainbow.desktop.popular

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.PostSorting
import com.rainbow.desktop.components.RainbowLazyColumn
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.post.posts
import com.rainbow.desktop.utils.getOrDefault

@Composable
fun PopularScreen(
    onNavigateMainScreen: (MainScreen) -> Unit,
    onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val stateHolder = remember { PopularScreenStateHolder.Instance }
    val postsState by stateHolder.postsStateHolder.items.collectAsState()
    val postSorting by stateHolder.postsStateHolder.sorting.collectAsState()
    val timeSorting by stateHolder.postsStateHolder.timeSorting.collectAsState()
    val selectedItemId by stateHolder.selectedItemId.collectAsState()
    val postLayout by stateHolder.postLayout.collectAsState()
    val posts = remember(postsState) { postsState.getOrDefault(emptyList()) }

    RainbowLazyColumn(modifier) {
        item {
            PostSorting(
                postSorting,
                timeSorting,
                stateHolder.postsStateHolder::setSorting,
                stateHolder.postsStateHolder::setTimeSorting,
            )
        }

        posts(
            posts,
            postLayout,
            onNavigateMainScreen,
            onNavigateDetailsScreen = { detailsScreen ->
                if (detailsScreen is DetailsScreen.Post) {
                    stateHolder.selectItemId(detailsScreen.postId)
                }
                onNavigateDetailsScreen(detailsScreen)
            },
            onShowSnackbar,
            stateHolder.postsStateHolder::setLastItem,
        )
    }

    DisposableEffect(selectedItemId) {
        selectedItemId?.let { postId ->
            onNavigateDetailsScreen(DetailsScreen.Post(postId))
        }
        onDispose {}
    }
}