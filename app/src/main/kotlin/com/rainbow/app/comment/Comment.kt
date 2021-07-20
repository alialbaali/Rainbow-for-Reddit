package com.rainbow.app.comment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.app.components.*
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.app.utils.defaultPadding
import com.rainbow.app.utils.displayTime
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.userDisplayName

@Composable
fun Commment(
    comment: Comment,
    onUpvote: (commentId: String) -> Unit,
    onDownvote: (commentId: String) -> Unit,
    onUnvote: (commentId: String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val awards = comment.awards

    Column(
        modifier
            .defaultPadding()
    ) {
        Row {
            Text(comment.userDisplayName)
            Dot()
            Text(comment.upvotesCount.toString(), color = MaterialTheme.colors.onBackground.copy(0.5F))
            Dot()
            Text(comment.creationDate.displayTime, color = MaterialTheme.colors.onBackground.copy(0.5F))
            if (awards.isNotEmpty()) {
                Dot()
//                Award()
                Text("${awards.count()} Awards")
            }
        }

        Text(comment.body)

        Row {
            UpvoteButton(
                onClick = {}
            )
            Spacer(Modifier.width(16.dp))
            DownvoteButton(
                onClick = {}
            )
            Spacer(Modifier.width(16.dp))
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
