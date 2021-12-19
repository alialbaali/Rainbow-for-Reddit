package com.rainbow.app.post

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.unit.dp
import com.rainbow.app.comment.AddComment
import com.rainbow.app.comment.postComments
import com.rainbow.app.components.RainbowIconButton
import com.rainbow.app.components.RainbowLazyColumn
import com.rainbow.app.components.SortingItem
import com.rainbow.app.utils.*
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Post

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostScreen(
    model: PostScreenModel,
    focusRequester: FocusRequester,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    onPostUpdate: (Post) -> Unit,
    onCommentUpdate: (Comment) -> Unit,
    modifier: Modifier = Modifier,
) {
    val postState by model.post.collectAsState()
    val backStack by model.backStack.collectAsState()
    val forwardStack by model.forwardStack.collectAsState()
    val commentListModel by model.commentListModel.collectAsState()
    val commentsState by commentListModel.comments.collectAsState()
    val commentsVisibility by commentListModel.commentsVisibility.collectAsState()
    val sorting by commentListModel.sorting.collectAsState()
    postState.composed(onShowSnackbar, modifier) { post ->
        RainbowLazyColumn(
            modifier
                .defaultSurfaceShape()
                .defaultPadding(),
            verticalArrangement = Arrangement.Top,
        ) {
            item {
                Post(post, onPostUpdate, focusRequester, onUserNameClick, onSubredditNameClick, onShowSnackbar)
                Spacer(Modifier.height(16.dp))
                AddComment(
                    post,
                    Modifier
                        .fillParentMaxWidth()
                        .focusOrder(focusRequester)
                )
                Spacer(Modifier.height(16.dp))
            }
            stickyHeader {
                Row(Modifier.fillParentMaxWidth().background(MaterialTheme.colors.surface)) {
                    CommentsActions(
                        backStack.isNotEmpty(),
                        forwardStack.isNotEmpty(),
                        model::back,
                        model::forward,
                        commentListModel::refreshComments,
                        commentListModel::expandComments,
                        commentListModel::collapseComments,
                        Modifier.weight(1F)
                    )
                    SortingItem(
                        sorting,
                        onSortingUpdate = { commentListModel.setSorting(it) },
                    )
                }
                Spacer(Modifier.height(16.dp))
            }
            postComments(
                commentsState,
                post.userName,
                commentsVisibility,
                setCommentsVisibility = { commentId, isVisible ->
                    commentListModel.setCommentVisibility(commentId, isVisible)
                },
                onUserNameClick,
                onSubredditNameClick,
                onRequestMoreComments = { commentId, moreComments ->
                    commentListModel.loadMoreComments(commentId, moreComments)
                },
                onCommentUpdate = onCommentUpdate,
                onRequestThreadComments = { parentId -> model.setCommentListModel(parentId) }
            )
        }
    }
}

@Composable
private fun CommentsActions(
    isBackEnabled: Boolean,
    isForwardEnabled: Boolean,
    onBackClick: () -> Unit,
    onForwardClick: () -> Unit,
    onRefresh: () -> Unit,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        RainbowIconButton(
            RainbowIcons.ArrowBack,
            RainbowStrings.NavigateBack,
            onBackClick,
            enabled = isBackEnabled,
        )

        RainbowIconButton(
            RainbowIcons.ArrowForward,
            RainbowStrings.NavigateForward,
            onForwardClick,
            enabled = isForwardEnabled
        )

        RainbowIconButton(
            RainbowIcons.Refresh,
            RainbowStrings.Refresh,
            onRefresh,
        )

        RainbowIconButton(
            RainbowIcons.UnfoldMore,
            RainbowStrings.ExpandComments,
            onExpand,
        )

        RainbowIconButton(
            RainbowIcons.UnfoldLess,
            RainbowStrings.CollapseComments,
            onCollapse,
        )
    }
}

@Composable
private fun Post(
    post: Post,
    onUpdate: (Post) -> Unit,
    focusRequester: FocusRequester,
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
                .wrapContentHeight()
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
            modifier = Modifier
                .heightIn(max = 600.dp)
                .fillMaxWidth()
        )

        PostActions(
            post,
            onUpdate,
            focusRequester,
            onShowSnackbar,
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )
    }
}
