package com.rainbow.app.post

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.unit.dp
import com.rainbow.app.comment.AddComment
import com.rainbow.app.comment.postComments
import com.rainbow.app.utils.*
import com.rainbow.domain.models.PostSorting

@Composable
fun PostScreen(
    postModel: PostModel<PostSorting>,
    focusRequester: FocusRequester,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedPostState by postModel.selectedPost.collectAsState()
    val scrollingState = rememberLazyListState()
    selectedPostState.composed(onShowSnackbar) { post ->
        if (post != null) {
            val model = remember(post.id) { PostScreenModel.getOrCreateInstance(post.id) }
            val commentsVisibility by model.commentModel.commentsVisibility.collectAsState()
            val commentsSorting by model.commentModel.commentsSorting.collectAsState()
            val timeSorting by model.commentModel.timeSorting.collectAsState()
            val commentsState by model.commentModel.comments.collectAsState()
            LazyColumn(
                modifier
                    .defaultSurfaceShape()
                    .defaultPadding(),
                scrollingState,
            ) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                            postModel,
                            focusRequester,
                            onShowSnackbar,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    AddComment(
                        post,
                        Modifier
                            .fillParentMaxWidth()
                            .focusOrder(focusRequester)
                    )
                    Spacer(Modifier.height(16.dp))
//                    PostSorting(
//                        commentsSorting,
//                        onSortingUpdate = { model.commentModel.setCommentSorting(it) },
//                        timeSorting,
//                        onTimeSortingUpdate = { model.commentModel.setTimeSorting(it) }
//                    )
                    Spacer(Modifier.height(16.dp))
                    if (commentsState.isSuccess)
                        Row(Modifier.fillParentMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            IconButton(
                                onClick = { },
                                modifier = Modifier.defaultSurfaceShape(shape = CircleShape)
                            ) {
                                Icon(RainbowIcons.ArrowBack, RainbowStrings.NavigateBack)
                            }

                            IconButton(
                                onClick = {},
                                modifier = Modifier.defaultSurfaceShape(shape = CircleShape)
                            ) {
                                Icon(RainbowIcons.ArrowForward, RainbowStrings.NavigateForward)
                            }

                            IconButton(
                                onClick = { model.commentModel.loadComments() },
                                Modifier.defaultSurfaceShape(shape = CircleShape)
                            ) {
                                Icon(RainbowIcons.Refresh, RainbowStrings.Refresh)
                            }

                            IconButton(
                                onClick = { model.commentModel.expandComments() },
                                modifier = Modifier.defaultSurfaceShape(shape = CircleShape)
                            ) {
                                Icon(RainbowIcons.UnfoldLess, RainbowStrings.ExpandComments)
                            }
                            IconButton(
                                onClick = { model.commentModel.collapseComments() },
                                modifier = Modifier.defaultSurfaceShape(shape = CircleShape)
                            ) {
                                Icon(RainbowIcons.UnfoldMore, RainbowStrings.CollapseComments)
                            }
                        }
                    Spacer(Modifier.height(16.dp))
                }
                postComments(
                    commentsState,
                    model.commentModel,
                    post.userName,
                    commentsVisibility,
                    setCommentsVisibility = { commentId, isVisible ->
                        model.commentModel.setCommentVisibility(commentId, isVisible)
                    },
                    onLoadMore = { },
                    onUserNameClick,
                    onSubredditNameClick,
                    onRequestMoreComments = { commentId, moreComments ->
                        model.commentModel.getMoreComments(post.id, commentId, moreComments)
                    }
                )
            }
            VerticalScrollbar(rememberScrollbarAdapter(scrollingState))
        }
    }
}
