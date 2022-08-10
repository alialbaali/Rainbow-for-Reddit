package com.rainbow.desktop.subreddit

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.rounded.PlaylistAdd
import androidx.compose.material.icons.rounded.PlaylistAddCheck
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.defaultSurfaceShape
import com.rainbow.domain.models.Subreddit


@Composable
fun SubredditItemName(subredditName: String, modifier: Modifier = Modifier) {
    Text(
        text = subredditName,
        modifier = modifier,
        fontSize = 26.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SubredditFavoriteIconButton(
    subreddit: Subreddit,
    onSubredditUpdate: (Subreddit) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    IconToggleButton(
        subreddit.isFavorite,
        onCheckedChange = { isFavorite ->
            if (isFavorite) {
                SubredditActionsStateHolder.favoriteSubreddit(subreddit, onSubredditUpdate)
                onShowSnackbar(RainbowStrings.FavoriteMessage(subreddit.name))
            } else {
                SubredditActionsStateHolder.unFavoriteSubreddit(subreddit, onSubredditUpdate)
                onShowSnackbar(RainbowStrings.UnFavoriteMessage(subreddit.name))
            }
        },
        modifier = modifier
            .defaultSurfaceShape(shape = CircleShape),
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
    onSubredditUpdate: (Subreddit) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = {
            if (subreddit.isSubscribed) {
                SubredditActionsStateHolder.unSubscribeSubreddit(subreddit, onSubredditUpdate)
                onShowSnackbar(RainbowStrings.UnsubscribeMessage(subreddit.name))
            } else {
                SubredditActionsStateHolder.subscribeSubreddit(subreddit, onSubredditUpdate)
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