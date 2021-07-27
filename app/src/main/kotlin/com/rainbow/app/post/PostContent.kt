package com.rainbow.app.post

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import com.rainbow.app.PagingEffect
import com.rainbow.app.utils.*
import com.rainbow.data.Repos
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.PostsSorting
import com.rainbow.domain.models.TimeSorting
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


sealed class PostScreenType {
    object Home : PostScreenType()
    data class User(val userName: String) : PostScreenType()
    data class Subreddit(val subredditName: String) : PostScreenType()
}

@Composable
fun PostContent(type: PostScreenType, modifier: Modifier = Modifier) {

    var currentSelectedPost by remember { mutableStateOf<String?>(null) }

    Row(modifier) {

        Posts(
            type = type,
            onSelectPost = {
                currentSelectedPost = it
            },
            modifier = Modifier.weight(1F)
        )

        currentSelectedPost?.let {
            PostScreen(it, Modifier.weight(1F))
        }

    }
}

@Composable
fun Posts(
    type: PostScreenType,
    onSelectPost: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val scope = rememberCoroutineScope()

    var lastPostId by remember { mutableStateOf<String?>(null) }

    var postSorting by remember { mutableStateOf(PostsSorting.Default) }

    var timeSorting by remember { mutableStateOf(TimeSorting.Default) }

    val state by produceState<UIState<List<Post>>>(UIState.Loading, postSorting, timeSorting, lastPostId) {
        Repos.Post.getHomePosts(postSorting, timeSorting, lastPostId)
            .map { it.toUIState() }
            .collect { value = it }
    }

        state.composed { posts ->

        if (posts.isNotEmpty())
            onSelectPost(posts.first().id)

        Column(modifier) {

            PostsSorting(
                postSorting,
                { postSorting = it }
            )

            LazyColumn {

                itemsIndexed(posts) { index, post ->

                    Post(
                        post,
                        onUpvote = {
                            scope.launch { Repos.Post.upvotePost(post.id) }
                        },
                        onDownvote = {
                            scope.launch {
                                Repos.Post.downvotePost(post.id)
                            }
                        },
                        onUnvote = {
                            scope.launch {
                                Repos.Post.unvotePost(post.id)
                            }
                        },
                        onShare = {},
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .wrapContentHeight()
                            .layoutId(post.id)
                            .clickable { onSelectPost(post.id) },
                    )

                    PagingEffect(posts, index) {
                        lastPostId = it.id
                    }

                }
            }
        }
    }
}

@Composable
private fun PostsSorting(
    postSorting: PostsSorting,
    onPostSortingUpdate: (PostsSorting) -> Unit,
    modifier: Modifier = Modifier
) {

    var isMenuVisible by remember { mutableStateOf(false) }

    val iconRotation by animateFloatAsState(if (isMenuVisible) 180F else 0F)

    Column(modifier) {
        Button(
            onClick = { isMenuVisible = !isMenuVisible },
            modifier = Modifier
                .defaultPadding(),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background)
        ) {
            Text(
                postSorting.name,
                style = MaterialTheme.typography.subtitle1
            )

            Icon(
                RainbowIcons.ArrowDropUp,
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
                    .rotate(iconRotation),
                tint = MaterialTheme.colors.onBackground,
            )
        }

        DropdownMenu(
            isMenuVisible,
            onDismissRequest = { isMenuVisible = !isMenuVisible },
        ) {
            PostsSorting.values()
                .forEach {

                    DropdownMenuItem(
                        onClick = {
                            onPostSortingUpdate(it)
                            isMenuVisible = false
                        },
                    ) {
                        Text(
                            text = it.name,
                            modifier = Modifier
                                .defaultPadding()
                                .wrapContentWidth()
                        )
                    }
                }
        }

    }

}