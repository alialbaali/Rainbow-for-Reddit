package com.rainbow.app.subreddit

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.PlaylistAdd
import androidx.compose.material.icons.rounded.PlaylistAddCheck
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.app.components.RainbowProgressIndicator
import com.rainbow.app.components.TextImage
import com.rainbow.app.utils.ImageBorderSize
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.defaultShape
import com.rainbow.data.Repos
import com.rainbow.domain.models.Subreddit
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource
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

@Composable
fun SubredditItemHeader(subreddit: Subreddit) {
    val bannerPainterResource = lazyPainterResource(subreddit.bannerImageUrl.toString())
    val painterResource = lazyPainterResource(subreddit.imageUrl.toString())
    Box {
        val BannerImageModifier = Modifier
            .height(100.dp)
            .fillMaxWidth()

        val ImageModifier = Modifier
            .padding(top = 50.dp)
            .border(ImageBorderSize, MaterialTheme.colors.background, MaterialTheme.shapes.large)
            .size(100.dp)
            .clip(MaterialTheme.shapes.large)
            .align(Alignment.BottomCenter)

        KamelImage(
            bannerPainterResource,
            contentDescription = subreddit.name,
            modifier = BannerImageModifier,
            contentScale = ContentScale.Crop,
            onLoading = { RainbowProgressIndicator(BannerImageModifier) },
            onFailure = {
                Image(
                    ColorPainter(
                        MaterialTheme.colors.primary,
                    ),
                    subreddit.name,
                    BannerImageModifier,
                )
            }
        )

        KamelImage(
            resource = painterResource,
            contentDescription = subreddit.name,
            modifier = ImageModifier,
            onLoading = { RainbowProgressIndicator(ImageModifier) },
            onFailure = { TextImage(subreddit.name, ImageModifier.background(MaterialTheme.colors.secondary)) }
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SubredditFavoriteIconButton(subreddit: Subreddit, onShowSnackbar: (String) -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
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
            .defaultShape(shape = CircleShape),
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
    modifier: Modifier = Modifier
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