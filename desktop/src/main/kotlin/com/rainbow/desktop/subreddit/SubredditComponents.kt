package com.rainbow.desktop.subreddit

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.rounded.PlaylistAdd
import androidx.compose.material.icons.rounded.PlaylistAddCheck
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.domain.models.Subreddit

@Composable
fun SubredditItemName(subredditName: String, modifier: Modifier = Modifier) {
    Text(
        text = subredditName,
        modifier = modifier,
        style = MaterialTheme.typography.titleLarge
    )
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SubredditFavoriteIconButton(
    subreddit: Subreddit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedIconToggleButton(
        checked = subreddit.isFavorite,
        onCheckedChange = { isFavorite ->
            if (isFavorite) {
                SubredditActionsStateHolder.favoriteSubreddit(subreddit)
                onShowSnackbar(RainbowStrings.FavoriteMessage(subreddit.name))
            } else {
                SubredditActionsStateHolder.unFavoriteSubreddit(subreddit)
                onShowSnackbar(RainbowStrings.UnFavoriteMessage(subreddit.name))
            }
        },
        modifier = modifier,
        enabled = enabled,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        colors = IconButtonDefaults.iconToggleButtonColors(checkedContentColor = Color.Transparent),
        shape = MaterialTheme.shapes.medium
    ) {
        AnimatedContent(subreddit.isFavorite) { isFavorite ->
            Icon(
                imageVector = if (isFavorite) RainbowIcons.Star else RainbowIcons.StarBorder,
                contentDescription = if (isFavorite) RainbowStrings.UnFavorite else RainbowStrings.Favorite,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SubscribeButton(
    subreddit: Subreddit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = {
            if (subreddit.isSubscribed) {
                SubredditActionsStateHolder.unSubscribeSubreddit(subreddit)
                onShowSnackbar(RainbowStrings.UnsubscribeMessage(subreddit.name))
            } else {
                SubredditActionsStateHolder.subscribeSubreddit(subreddit)
                onShowSnackbar(RainbowStrings.SubscribeMessage(subreddit.name))
            }
        },
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(RainbowTheme.dpDimensions.medium)
    ) {
        AnimatedContent(subreddit.isSubscribed) { isSubscribed ->
            Icon(
                if (isSubscribed) RainbowIcons.PlaylistAddCheck else RainbowIcons.PlaylistAdd,
                if (isSubscribed) RainbowStrings.Subscribed else RainbowStrings.Subscribe,
            )
        }
        Spacer(Modifier.width(RainbowTheme.dpDimensions.medium))
        AnimatedContent(subreddit.isSubscribed) { isSubscribed ->
            Text(
                if (isSubscribed) RainbowStrings.Subscribed else RainbowStrings.Subscribe,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}