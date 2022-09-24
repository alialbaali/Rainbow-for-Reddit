package com.rainbow.desktop.subreddit

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.PlaylistAdd
import androidx.compose.material.icons.rounded.PlaylistAddCheck
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.rainbow.desktop.components.RainbowButton
import com.rainbow.desktop.components.RainbowIconToggleButton
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.toColor
import com.rainbow.domain.models.Subreddit
import com.rainbow.domain.models.primaryColor

@Composable
fun SubredditItemName(subredditName: String, modifier: Modifier = Modifier) {
    Text(
        text = subredditName,
        modifier = modifier,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface,
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
    RainbowIconToggleButton(
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
        checkedContentColor = MaterialTheme.colorScheme.primary,
        checkedContainerColor = MaterialTheme.colorScheme.background,
        hoverContentColor = MaterialTheme.colorScheme.primary.copy(0.5F),
    ) {
        AnimatedContent(subreddit.isFavorite) { isFavorite ->
            Icon(
                imageVector = if (isFavorite) RainbowIcons.Favorite else RainbowIcons.FavoriteBorder,
                contentDescription = if (isFavorite) RainbowStrings.UnFavorite else RainbowStrings.Favorite,
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
    RainbowButton(
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
        // Needs check for content color for the right contrast. Maybe checking < or > than a certain value.
//        containerColor = subreddit.primaryColor?.toColor() ?: MaterialTheme.colorScheme.primary,
        containerColor = MaterialTheme.colorScheme.primary,
    ) {
        AnimatedContent(subreddit.isSubscribed) { isSubscribed ->
            Icon(
                if (isSubscribed) RainbowIcons.PlaylistAddCheck else RainbowIcons.PlaylistAdd,
                if (isSubscribed) RainbowStrings.Subscribed else RainbowStrings.Subscribe,
            )
        }
        AnimatedContent(subreddit.isSubscribed) { isSubscribed ->
            Text(
                if (isSubscribed) RainbowStrings.Subscribed else RainbowStrings.Subscribe,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}