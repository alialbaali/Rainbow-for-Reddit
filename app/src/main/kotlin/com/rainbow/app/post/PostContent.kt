package com.rainbow.app.post

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import com.rainbow.domain.models.PostListSorting
import com.rainbow.domain.models.TimeSorting
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime


sealed class PostListType {
    object Home : PostListType()
    data class User(val userName: String) : PostListType()
    data class Subreddit(val subredditName: String) : PostListType()
}

@Composable
fun PostContent(type: PostListType, modifier: Modifier = Modifier) {

    var currentSelectedPost by remember { mutableStateOf<String?>(null) }

    Row(modifier) {

        PostList(
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostList(
    type: PostListType,
    onSelectPost: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val scope = rememberCoroutineScope()

    var lastPostId by remember { mutableStateOf<String?>(null) }

    var postSorting by remember { mutableStateOf(PostListSorting.Default) }

    var timeSorting by remember { mutableStateOf(TimeSorting.Default) }

    val state by produceState<UIState<List<Post>>>(UIState.Loading, postSorting, timeSorting, lastPostId) {
        Repos.Post.getHomePosts(postSorting, timeSorting, lastPostId)
            .map { it.toUIState() }
            .collect { value = it }
    }

    state.composed { posts ->

        if (posts.isNotEmpty())
            onSelectPost(posts.first().id)

        LazyColumn(modifier) {

            stickyHeader {
                Box(
                    Modifier
                        .background(MaterialTheme.colors.background)
                        .fillParentMaxWidth()
                ) {
                    PostListSortingItem(
                        postSorting,
                        { postSorting = it }
                    )
                }
            }

            itemsIndexed(posts) { index, post ->

                PostItem(
                    post,
                    onUpvote = {
                        scope.launch {
                            Repos.Post.upvotePost(post.id)
                        }
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
                        .layoutId(post.id),
                    onClick = {
                        onSelectPost(post.id)
                    }
                )

                PagingEffect(posts, index) {
                    lastPostId = it.id
                }

            }
        }
    }
}

@Composable
private fun PostListSortingItem(
    postSorting: PostListSorting,
    onPostSortingUpdate: (PostListSorting) -> Unit,
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
            PostListSorting.values()
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

@Preview
@Composable
private fun PostItemPreview() {
    PostItem(
        post = Post(
            id = "",
            userId = "",
            userName = "LoneWalker20",
            subredditId = "",
            subredditName = "Apple",
            title = "This is rainbow",
            type = Post.Type.Text("This is body"),
            upvotesCount = 30UL,
            upvotesRatio = 4.4,
            commentsCount = 30UL,
            creationDate = LocalDateTime.now(),
        ),
        onClick = {},
        onUpvote = {},
        onDownvote = {},
        onUnvote = {},
        onShare = {}
    )
}