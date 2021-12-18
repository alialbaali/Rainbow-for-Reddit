package com.rainbow.app.subreddit

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.PlaylistAdd
import androidx.compose.material.icons.rounded.PlaylistAddCheck
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.defaultSurfaceShape
import com.rainbow.data.Repos
import com.rainbow.domain.models.Subreddit
import kotlinx.coroutines.launch

@Composable
fun SubredditItemName(subredditName: String, modifier: Modifier = Modifier) {
    Text(
        text = subredditName,
        modifier = modifier,
        fontSize = 26.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colors.onBackground
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
    val scope = rememberCoroutineScope()
    IconToggleButton(
        subreddit.isFavorite,
        onCheckedChange = { isFavorite ->
            scope.launch {
                if (isFavorite) {
                    Repos.Subreddit.favoriteSubreddit(subreddit.name)
                    onShowSnackbar(RainbowStrings.FavoriteMessage(subreddit.name))
                } else {
                    Repos.Subreddit.unFavoriteSubreddit(subreddit.name)
                    onShowSnackbar(RainbowStrings.UnFavoriteMessage(subreddit.name))
                }
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
                tint = if (isFavorite) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground
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
    val scope = rememberCoroutineScope()
    Button(
        onClick = {
            scope.launch {
                if (subreddit.isSubscribed) {
                    Repos.Subreddit.unSubscribeSubreddit(subreddit.name)
                    onShowSnackbar(RainbowStrings.UnsubscribeMessage(subreddit.name))
                } else {
                    Repos.Subreddit.subscribeSubreddit(subreddit.name)
                    onShowSnackbar(RainbowStrings.SubscribeMessage(subreddit.name))
                }
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