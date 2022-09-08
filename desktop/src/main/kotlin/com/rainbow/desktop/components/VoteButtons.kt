package com.rainbow.desktop.components

import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.rainbow.desktop.utils.RainbowIcons

@Composable
fun UpvoteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageVector: ImageVector = RainbowIcons.ArrowUpward,
    tint: Color = Color.Black,
    enabled: Boolean = true,
) = VoteButton(onClick, imageVector, modifier, enabled, tint)

@Composable
fun DownvoteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageVector: ImageVector = RainbowIcons.ArrowDownward,
    tint: Color = Color.Black,
    enabled: Boolean = true,
) = VoteButton(onClick, imageVector, modifier, enabled, tint)

@Composable
private fun VoteButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tint: Color = Color.Black,
) {
    IconButton(onClick, modifier, enabled) {
        Icon(imageVector = imageVector, tint = tint, contentDescription = imageVector.name)
    }
}
