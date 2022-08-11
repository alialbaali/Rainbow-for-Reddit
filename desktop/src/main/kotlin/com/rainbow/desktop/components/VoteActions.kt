package com.rainbow.desktop.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.desktop.utils.defaultBackgroundShape
import com.rainbow.desktop.utils.format
import com.rainbow.domain.models.Vote

@Composable
fun VoteActions(
    vote: Vote,
    votesCount: Int,
    onUpvote: () -> Unit,
    onDownvote: () -> Unit,
    onUnvote: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val borderColor by animateColorAsState(
        when (vote) {
            Vote.Up -> MaterialTheme.colorScheme.primary
            Vote.Down -> MaterialTheme.colorScheme.secondary
            Vote.None -> MaterialTheme.colorScheme.onBackground.copy(0.1F)
        }
    )

    val contentColor by animateColorAsState(
        when (vote) {
            Vote.Up -> MaterialTheme.colorScheme.primary
            Vote.Down -> MaterialTheme.colorScheme.secondary
            Vote.None -> MaterialTheme.colorScheme.onBackground
        }
    )

    val updatedVotesCount by animateIntAsState(
        when (vote) {
            Vote.Up -> votesCount.inc()
            Vote.Down -> votesCount.dec()
            Vote.None -> votesCount
        }
    )

    Row(
        modifier
            .defaultBackgroundShape(borderColor = borderColor, shape = CircleShape),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        UpvoteButton(
            onClick = {
                when (vote) {
                    Vote.Up -> onUnvote()
                    else -> onUpvote()
                }
            },
            tint = contentColor,
        )
        Text(updatedVotesCount.format(), fontSize = 14.sp, color = contentColor)
        DownvoteButton(
            onClick = {
                when (vote) {
                    Vote.Down -> onUnvote()
                    else -> onDownvote()
                }
            },
            tint = contentColor,
        )
    }
}