package com.rainbow.desktop.components

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings
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
    val animatedVotesCount by animateIntAsState(
        when (vote) {
            Vote.Up -> votesCount.inc()
            Vote.Down -> votesCount.dec()
            Vote.None -> votesCount
        }
    )

    Row(
        modifier.background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(RainbowTheme.dpDimensions.small)
    ) {
        RainbowIconToggleButton(
            checked = vote == Vote.Up,
            onCheckedChange = {
                when (vote) {
                    Vote.Up -> onUnvote()
                    else -> onUpvote()
                }
            },
            contentColor = MaterialTheme.colorScheme.onSurface,
            checkedContentColor = MaterialTheme.colorScheme.primary,
            hoverContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5F),
        ) {
            Icon(RainbowIcons.ArrowUpward, RainbowStrings.Upvote)
        }
        Text(animatedVotesCount.format(), style = MaterialTheme.typography.labelLarge)
        RainbowIconToggleButton(
            checked = vote == Vote.Down,
            onCheckedChange = {
                when (vote) {
                    Vote.Down -> onUnvote()
                    else -> onDownvote()
                }
            },
            contentColor = MaterialTheme.colorScheme.onSurface,
            checkedContentColor = MaterialTheme.colorScheme.secondary,
            hoverContentColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5F),
        ) {
            Icon(RainbowIcons.ArrowDownward, RainbowStrings.Downvote)
        }
    }
}