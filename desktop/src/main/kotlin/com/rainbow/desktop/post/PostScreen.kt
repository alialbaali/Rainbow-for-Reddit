package com.rainbow.desktop.post

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.comment.postComments
import com.rainbow.desktop.components.*
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.ui.dimensions
import com.rainbow.desktop.utils.*
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.PostCommentSorting
import com.rainbow.domain.models.PostLayout
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

private val LazyImageSize = 150.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostScreen(
    postId: String,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val stateHolder = remember(postId) { PostScreenStateHolder(postId) }
    val scrollingState = rememberLazyListState()
    val postState by stateHolder.post.collectAsState()
    val commentsState by stateHolder.comments.collectAsState()
    val post = remember(postState) { postState.getOrNull() }
    val commentSorting by stateHolder.commentSorting.collectAsState()
    val backStack by stateHolder.backStack.collectAsState()
    val forwardStack by stateHolder.forwardStack.collectAsState()
    val commentsVisibility by stateHolder.commentsVisibility.collectAsState()
    val firstVisibleItemIndex = scrollingState.firstVisibleItemIndex
    val topCorner by animateDpAsState(
        if (firstVisibleItemIndex >= 1) {
            0.dp
        } else {
            MaterialTheme.dimensions.medium
        }
    )

    RainbowLazyColumn(
        modifier.background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Top,
        scrollingState = scrollingState,
    ) {
        if (post != null) {
            item {
                Post(
                    post,
                    onUserNameClick = { userName -> onNavigateMainScreen(MainScreen.User(userName)) },
                    onSubredditNameClick = { subredditName -> onNavigateMainScreen(MainScreen.Subreddit(subredditName)) },
                    onShowSnackbar,
                    Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .shadow(1.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .fillParentMaxWidth()
                        .defaultPadding()
                )
                Spacer(Modifier.height(16.dp))
            }
            stickyHeader {
                CommentActions(
                    commentSorting,
                    backStack.isNotEmpty(),
                    forwardStack.isNotEmpty(),
                    stateHolder::setCommentSorting,
                    stateHolder::refreshComments,
                    stateHolder::expandComments,
                    stateHolder::collapseComments,
                    stateHolder::back,
                    stateHolder::forward,
                    shape = MaterialTheme.shapes.medium.copy(
                        topStart = CornerSize(topCorner),
                        topEnd = CornerSize(topCorner)
                    )
                )
                Spacer(Modifier.height(16.dp))
            }
            postComments(
                commentsState.map { it.comments },
                post.userName,
                commentsVisibility,
                onCommentVisibilityChanged = stateHolder::setCommentVisibility,
                onUserNameClick = { userName -> onNavigateMainScreen(MainScreen.User(userName)) },
                onSubredditNameClick = { subredditName -> onNavigateMainScreen(MainScreen.Subreddit(subredditName)) },
                onRequestMoreComments = stateHolder::loadMoreComments,
                onRequestThreadComments = stateHolder::setThreadParentId,
                onShowSnackbar,
            )
        }

        if (postState.isLoading) {
            item { RainbowProgressIndicator() }
        }
    }
}


@Composable
private fun Post(
    post: Post,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(RainbowTheme.dimensions.medium)) {
        PostInfo(
            post = post,
            onUserNameClick,
            onSubredditNameClick,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )

        if (post.flair.types.isNotEmpty()) FlairItem(post.flair, FlairStyle.Default)

        PostTitle(
            title = post.title,
            isRead = false,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

        post.body?.let { body ->
            PostBody(
                body = body,
                postLayout = PostLayout.Large,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }

        PostContent(
            post = post,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )

        PostOptions(
            post,
            onShowSnackbar,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun PostContent(post: Post, modifier: Modifier = Modifier) {
    when (val type = post.type) {
        is Post.Type.Image -> {
            val urls = remember(type) { type.urls }
            if (urls.size > 1) {
                var selectedUrl by remember(urls) { mutableStateOf(urls.first()) }
                val selectedUrlIndex = remember(selectedUrl) { urls.indexOf(selectedUrl) }
                val isForwardEnabled = remember(selectedUrlIndex) { urls.getOrNull(selectedUrlIndex + 1) != null }
                val isBackEnabled = remember(selectedUrlIndex) { urls.getOrNull(selectedUrlIndex - 1) != null }
                var slideDirection by remember { mutableStateOf(AnimatedContentScope.SlideDirection.Left) }

                Box(modifier = Modifier.clip(MaterialTheme.shapes.medium)) {
                    AnimatedContent(
                        selectedUrl,
                        transitionSpec = { slideIntoContainer(slideDirection) with slideOutOfContainer(slideDirection) }
                    ) { url ->
                        val painterResource = lazyPainterResource(url)
                        KamelImage(
                            painterResource,
                            contentDescription = post.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillWidth,
                            onLoading = { RainbowProgressIndicator(Modifier.fillMaxSize()) },
                        )
                    }

                    RainbowIconButton(
                        onClick = {
                            slideDirection = AnimatedContentScope.SlideDirection.Left
                            selectedUrl = urls[selectedUrlIndex + 1]
                        },
                        modifier = Modifier.align(Alignment.CenterEnd).padding(RainbowTheme.dimensions.medium),
                        enabled = isForwardEnabled,
                    ) {
                        Icon(RainbowIcons.ArrowForward, RainbowStrings.NavigateForward)
                    }

                    RainbowIconButton(
                        onClick = {
                            slideDirection = AnimatedContentScope.SlideDirection.Right
                            selectedUrl = urls[selectedUrlIndex - 1]
                        },
                        modifier = Modifier.align(Alignment.CenterStart).padding(RainbowTheme.dimensions.medium),
                        enabled = isBackEnabled,
                    ) {
                        Icon(RainbowIcons.ArrowBack, RainbowStrings.NavigateBack)
                    }
                }

                PostImages(
                    title = post.title,
                    type = type,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    onSelectChange = {
                        val index = urls.indexOf(it)
                        slideDirection = if (index > selectedUrlIndex) {
                            AnimatedContentScope.SlideDirection.Left
                        } else {
                            AnimatedContentScope.SlideDirection.Right
                        }
                        selectedUrl = it
                    },
                    selectedUrl = selectedUrl
                )
            } else {
                PostContent(
                    post = post,
                    modifier = modifier,
                    postLayout = PostLayout.Large,
                )
            }
        }

        else -> PostContent(
            post = post,
            modifier = modifier,
            postLayout = PostLayout.Large,
        )
    }
}

@Composable
private fun PostImages(
    selectedUrl: String,
    title: String,
    type: Post.Type.Image,
    onSelectChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium),
        horizontalArrangement = Arrangement.spacedBy(RainbowTheme.dimensions.medium),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(RainbowTheme.dimensions.medium)
    ) {
        items(type.urls) { url ->
            val resource = lazyPainterResource(url)
            Box {
                KamelImage(
                    resource,
                    title,
                    Modifier
                        .clip(MaterialTheme.shapes.small)
                        .clickable { onSelectChange(url) }
                        .background(MaterialTheme.colorScheme.surface)
                        .size(LazyImageSize),
                    contentScale = ContentScale.Crop,
                    onLoading = { RainbowProgressIndicator(Modifier.size(LazyImageSize)) },
                )

                AnimatedVisibility(
                    visible = selectedUrl == url,
                    Modifier
                        .padding(RainbowTheme.dimensions.small)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        RainbowIcons.CheckCircle,
                        RainbowStrings.Selected,
                        tint = Color.White,
                    )
                }
            }
        }
    }
}

@Composable
private fun LazyItemScope.CommentActions(
    sorting: PostCommentSorting,
    isBackEnabled: Boolean,
    isForwardEnabled: Boolean,
    setCommentSorting: (PostCommentSorting) -> Unit,
    onRefresh: () -> Unit,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    onBackClick: () -> Unit,
    onForwardClick: () -> Unit,
    shape: Shape = MaterialTheme.shapes.medium,
) {
    Surface(
        modifier = Modifier.fillParentMaxWidth(),
        shape = shape
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(RainbowTheme.dimensions.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OptionsSurface {
                RainbowIconButton(
                    onClick = onBackClick,
                    enabled = isBackEnabled,
                ) {
                    Icon(RainbowIcons.ArrowBack, RainbowStrings.NavigateBack)
                }

                RainbowIconButton(
                    onClick = onForwardClick,
                    enabled = isForwardEnabled,
                ) {
                    Icon(
                        RainbowIcons.ArrowForward,
                        RainbowStrings.NavigateForward,
                    )
                }

                RainbowIconButton(
                    onClick = onRefresh,
                ) {
                    Icon(
                        RainbowIcons.Refresh,
                        RainbowStrings.Refresh
                    )
                }
            }

            OptionsSurface {
                RainbowIconButton(
                    onClick = onExpand,
                ) {
                    Icon(
                        RainbowIcons.UnfoldMore,
                        RainbowStrings.ExpandComments,
                    )
                }
                RainbowIconButton(
                    onClick = onCollapse,
                ) {
                    Icon(
                        RainbowIcons.UnfoldLess,
                        RainbowStrings.CollapseComments,
                    )
                }
            }

            ItemSorting(
                sorting,
                setCommentSorting,
                containerColor = MaterialTheme.colorScheme.background
            )
        }
    }
}
