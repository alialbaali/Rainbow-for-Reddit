package com.rainbow.app.comment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.app.components.RainbowMenu
import com.rainbow.app.components.RainbowMenuItem
import com.rainbow.app.components.VoteActions
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.domain.models.Comment

@Composable
fun PostCommentItem(
    comment: Comment,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        CommentInfo(comment)
        Spacer(Modifier.height(8.dp))
        Text(comment.body, color = MaterialTheme.colors.onBackground)
        Spacer(Modifier.height(8.dp))
        Row {
            VoteActions(
                vote = comment.vote,
                votesCount = comment.upvotesCount.toLong(),
                onUpvote = {},
                onDownvote = {},
                onUnvote = {}
            )
            RainbowMenu(
                expanded = false,
                onDismissRequest = {},
            ) {

                RainbowMenuItem("Share", RainbowIcons.Share) {

                }
                RainbowMenuItem("View User", RainbowIcons.Person) {

                }
                RainbowMenuItem("Reply", RainbowIcons.Send) {

                }
                RainbowMenuItem("Block User", RainbowIcons.Block) {

                }

            }
        }
    }
}
