package com.rainbow.desktop.post

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.comment.postComments
import com.rainbow.desktop.components.*
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.ui.dpDimensions
import com.rainbow.desktop.utils.*
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.PostCommentSorting

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
            MaterialTheme.dpDimensions.medium
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
                onViewMoreClick = stateHolder::loadViewMoreComments,
                onContinueThreadClick = stateHolder::setThreadParentId,
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
    Column(modifier, verticalArrangement = Arrangement.spacedBy(RainbowTheme.dpDimensions.medium)) {
        PostInfo(
            post = post,
            onUserNameClick,
            onSubredditNameClick,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            onAwardsClick = {}
        )

        if (post.flair.types.isNotEmpty()) FlairItem(post.flair, FlairStyle.Default)

        PostTitle(
            title = post.title,
            isRead = false,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

        PostContent(
            post = post,
            modifier = Modifier.fillMaxWidth()
        )

        PostActions(
            post,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )
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
                .padding(RainbowTheme.dpDimensions.medium),
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
