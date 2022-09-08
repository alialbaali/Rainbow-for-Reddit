package com.rainbow.desktop.post

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.comment.postComments
import com.rainbow.desktop.components.RainbowLazyColumn
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.utils.defaultPadding
import com.rainbow.desktop.utils.getOrNull
import com.rainbow.desktop.utils.map
import com.rainbow.domain.models.Post

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostScreen(
    postId: String,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val stateHolder = remember(postId) { PostScreenStateHolder(postId) }
    val postState by stateHolder.post.collectAsState()
    val commentsState by stateHolder.comments.collectAsState()
    val post = remember(postState) { postState.getOrNull() }
    val commentSorting by stateHolder.commentSorting.collectAsState()
    val backStack by stateHolder.backStack.collectAsState()
    val forwardStack by stateHolder.forwardStack.collectAsState()
    val commentsVisibility by stateHolder.commentsVisibility.collectAsState()

    RainbowLazyColumn(
        modifier.background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Top,
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
                )
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
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PostInfo(
            post = post,
            onUserNameClick,
            onSubredditNameClick,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            onAwardsClick = {}
        )

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
