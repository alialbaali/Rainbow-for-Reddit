package com.rainbow.app.comment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.rainbow.app.comment.CommentViewModel.CommentIntent
import com.rainbow.app.utils.*

@Composable
fun CommentContent(postId: String, modifier: Modifier = Modifier, postContent: @Composable LazyItemScope.() -> Unit) {

    val (state, emitIntent) = rememberViewModel(ViewModels.Comment)

    remember { emitIntent(CommentIntent.GetPostComments(postId)) }

    state.comments.composed { comments ->

        Box(modifier) {

            LazyColumn {

                item { postContent() }

                itemsIndexed(comments) { index, comment ->

                    Commment(
                        comment = comment,
                        onUpvote = { commentId -> emitIntent(CommentIntent.Upvote(commentId)) },
                        onDownvote = { commentId -> emitIntent(CommentIntent.Downvote(commentId)) },
                        onUnvote = { commentId -> emitIntent(CommentIntent.Unvote(commentId)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .layoutId(comment.id)
                    )

                }
            }

            AddComment(
                Modifier
                    .fillMaxWidth()
                    .defaultPadding()
                    .zIndex(1F)
                    .align(Alignment.BottomCenter)
                    .shadow(4.dp, MaterialTheme.shapes.medium)
            )

        }


//                .drawBehind {
//                    drawLine(
//                        color = Color.Black,
//                        start = Offset(0F, 0F),
//                        end = Offset(size.height, size.height)
//                    )
//                }
    }

}