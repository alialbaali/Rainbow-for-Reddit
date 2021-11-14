package com.rainbow.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.domain.models.Vote

@Composable
fun VoteActions(
    vote: Vote,
    votesCount: Long,
    onUpvote: () -> Unit,
    onDownvote: () -> Unit,
    onUnvote: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        UpvoteButton(
            onClick = {
                when (vote) {
                    Vote.Up -> onUnvote()
                    else -> onUpvote()
                }
            },
            tint = when (vote) {
                Vote.Up -> MaterialTheme.colors.primary
                else -> MaterialTheme.colors.onBackground
            },
            modifier = Modifier
                .background(
                    when (vote) {
                        Vote.Up -> MaterialTheme.colors.primary.copy(0.1F)
                        else -> MaterialTheme.colors.background
                    },
                    CircleShape,
                )
        )

        Spacer(Modifier.width(8.dp))

        val updatedVotesCount = when (vote) {
            Vote.Up -> votesCount.inc()
            Vote.Down -> votesCount.dec()
            Vote.None -> votesCount
        }

        Text(updatedVotesCount.toString(), fontSize = 14.sp)

        Spacer(Modifier.width(8.dp))

        DownvoteButton(
            onClick = {
                when (vote) {
                    Vote.Down -> onUnvote()
                    else -> onDownvote()
                }
            },
            tint = when (vote) {
                Vote.Down -> MaterialTheme.colors.secondary
                else -> MaterialTheme.colors.onBackground
            },
            modifier = Modifier
                .background(
                    when (vote) {
                        Vote.Down -> MaterialTheme.colors.secondary.copy(0.1F)
                        else -> MaterialTheme.colors.background
                    },
                    CircleShape,
                )
        )
    }
}