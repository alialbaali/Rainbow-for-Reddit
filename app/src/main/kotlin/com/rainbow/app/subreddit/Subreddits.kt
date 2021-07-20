package com.rainbow.app.subreddit

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.composed
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Subreddits(modifier: Modifier = Modifier) {

    val scope = rememberCoroutineScope()

    val state by Repos.SubredditRepo.getMySubreddits(null)
        .map { it.toUIState() }
        .collectAsState(UIState.Loading)

    var cellCount by remember { mutableStateOf(3) }

    state.composed { subreddits ->

        LazyVerticalGrid(
            GridCells.Fixed(animateIntAsState(cellCount).value),
            modifier.onSizeChanged { size ->
                cellCount = when {
                    size.width < 1000 -> 2
                    size.width in 1000 until 1500 -> 3
                    else -> 4
                }
            }
        ) {

            itemsIndexed(subreddits) { index, subreddit ->
                Subreddit(
                    subreddit = subreddit,
                    onClick = {},
                    onSubscribe = { scope.launch { Repos.SubredditRepo.subscribeSubreddit(it) } },
                    onUnSubscribe = { scope.launch { Repos.SubredditRepo.unSubscribeSubreddit(it) } },
                    onFavorite = { scope.launch { Repos.SubredditRepo.favoriteSubreddit(it) } },
                    onUnFavorite = { scope.launch { Repos.SubredditRepo.unFavoriteSubreddit(it) } },
                    modifier = Modifier
                        .fillParentMaxWidth(0.25F),
                )

//                PagingEffect(subreddits, index) {
//                    emitIntent(SubredditIntent.GetMySubreddits(it.id))
//                }
            }
        }
    }
}
