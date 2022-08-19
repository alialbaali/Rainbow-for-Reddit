package com.rainbow.desktop.subreddit

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.rounded.PlaylistAdd
import androidx.compose.material.icons.rounded.PlaylistAddCheck
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SubredditFavoriteIconButton(
    subreddit: Subreddit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    IconToggleButton(
        subreddit.isFavorite,
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
        enabled = enabled
    ) {
        AnimatedContent(subreddit.isFavorite) { isFavorite ->
            Icon(
                if (isFavorite) RainbowIcons.Star else RainbowIcons.StarBorder,
                if (isFavorite) RainbowStrings.UnFavorite else RainbowStrings.Favorite,
                tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
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
    ) {
        AnimatedContent(subreddit.isSubscribed) { isSubscribed ->
            Icon(
                if (isSubscribed) RainbowIcons.PlaylistAddCheck else RainbowIcons.PlaylistAdd,
                if (isSubscribed) RainbowStrings.Subscribed else RainbowStrings.Subscribe,
            )
        }
        Spacer(Modifier.width(16.dp))
        AnimatedContent(subreddit.isSubscribed) { isSubscribed ->
            Text(
                if (isSubscribed) RainbowStrings.Subscribed else RainbowStrings.Subscribe,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}