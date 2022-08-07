package com.rainbow.desktop.post

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.post.*
import com.rainbow.desktop.utils.composed
import com.rainbow.desktop.utils.defaultBackgroundShape
import com.rainbow.desktop.utils.defaultPadding
import com.rainbow.desktop.comment.AddComment
import com.rainbow.desktop.comment.postComments
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Post

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostScreen(
    type: PostScreenModel.Type,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onPostUpdate: (Post) -> Unit,
    onCommentUpdate: (Comment) -> Unit,
    stickyHeaderContent: @Composable (LazyItemScope.(PostScreenModel) -> Unit)? = null,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val model = PostScreenModel.getOrCreateInstance(type)
    val postState by model.post.collectAsState()
    val commentListModel by model.commentListModel.collectAsState()
    val commentsState by commentListModel.comments.collectAsState()
    val commentsVisibility by commentListModel.commentsVisibility.collectAsState()
    postState.composed(onShowSnackbar, modifier) { post ->
        LazyColumn(
            modifier.defaultBackgroundShape(),
            verticalArrangement = Arrangement.Top,
        ) {
            item {
                Post(
                    post,
                    onPostUpdate,
                    onUserNameClick,
                    onSubredditNameClick,
                    onShowSnackbar,
                    Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .defaultPadding()
                )
                Spacer(Modifier.height(16.dp))
                AddComment(
                    post,
                    Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillParentMaxWidth()
                        .defaultPadding()
                )
                Spacer(Modifier.height(16.dp))
            }
            if (stickyHeaderContent != null)
                stickyHeader { stickyHeaderContent(model) }
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
                onRequestThreadComments = { parentId -> model.setCommentListModel(parentId) },
            )
        }
    }
}


@Composable
private fun Post(
    post: Post,
    onUpdate: (Post) -> Unit,
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
            style = MaterialTheme.typography.headlineLarge,
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
            {},
            onUpdate,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {

        }
    }
}
