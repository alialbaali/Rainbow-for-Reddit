package com.rainbow.app.subreddit

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.app.components.RainbowProgressIndicator
import com.rainbow.app.utils.*
import com.rainbow.data.Repos
import com.rainbow.domain.models.Subreddit
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SubredditItem(
    subreddit: Subreddit,
    onClick: (Subreddit) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .heightIn(min = 150.dp)
            .then(ShapeModifier)
            .clickable { onClick(subreddit) },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Header(subreddit)

        Text(
            text = subreddit.name,
            modifier = Modifier
                .defaultPadding(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.onBackground
        )

        Text(
            subreddit.description,
            Modifier
                .defaultPadding(),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )

        SubredditInfoItems(subreddit)

        Row(
            modifier = Modifier
                .defaultPadding()
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SubscribeButton(subreddit)
            CreatePostButton()
            FavoriteToggleButton(subreddit)
        }
    }
}

@Composable
private fun SubredditInfoItems(subreddit: Subreddit, modifier: Modifier = Modifier) {
    subreddit.infoItems
        .forEach { item ->
            Row(
                modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(item.imageVector, item.key, Modifier.size(20.dp), tint = Color.Gray)
                    Text(item.key, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
                }
                Text(item.value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
            }
        }
}

@Composable
private fun SubscribeButton(subreddit: Subreddit, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val interactionSource = MutableInteractionSource()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val buttonText = if (isHovered) RainbowStrings.UnSubscribe else RainbowStrings.Subscribed
    Button(
        onClick = {
            scope.launch {
                if (subreddit.isSubscribed)
                    Repos.Subreddit.unSubscribeSubreddit(subreddit.name)
                else
                    Repos.Subreddit.subscribeSubreddit(subreddit.name)
            }
        },
        modifier = modifier
            .hoverable(MutableInteractionSource())
    ) {
        Text(buttonText, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun CreatePostButton(modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = {

        },
        modifier = modifier,
    ) {
        Text(RainbowStrings.CreatePost)
    }
}

@Composable
private fun FavoriteToggleButton(subreddit: Subreddit, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    IconToggleButton(
        subreddit.isFavorite,
        { isFavorite ->
            scope.launch {
                if (isFavorite)
                    Repos.Subreddit.favoriteSubreddit(subreddit.name)
                else
                    Repos.Subreddit.unFavoriteSubreddit(subreddit.name)
            }
        },
        modifier = modifier
            .background(MaterialTheme.colors.secondary, MaterialTheme.shapes.large)
    ) {
        Icon(if (subreddit.isFavorite) RainbowIcons.Star else RainbowIcons.StarBorder, null)
    }
}

@Composable
private fun Header(subreddit: Subreddit) {
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
            onFailure = {
                Image(
                    ColorPainter(MaterialTheme.colors.secondary),
                    subreddit.name,
                    ImageModifier
                )
            }
        )
    }
}

private data class SubredditInfoItem(val key: String, val value: String, val imageVector: ImageVector)

private val Subreddit.infoItems
    get() = listOf(
        SubredditInfoItem(
            RainbowStrings.Subscribers,
            subscribersCount.toString(),
            RainbowIcons.People
        ),
        SubredditInfoItem(
            RainbowStrings.ActiveSubscribers,
            activeSubscribersCount.toString(),
            RainbowIcons.EmojiPeople
        ),
        SubredditInfoItem(
            RainbowStrings.CrosspostEnabled,
            RainbowStrings.True,
            RainbowIcons.PostAdd
        ),
        SubredditInfoItem(
            RainbowStrings.Created,
            creationDate.displayTime,
            RainbowIcons.CalendarToday
        ),
        SubredditInfoItem(
            RainbowStrings.Type,
            type.name,
            RainbowIcons.Public
        ),
        SubredditInfoItem(
            RainbowStrings.NSFW,
            isNSFW.toString().replaceFirstChar { it.uppercase() },
            RainbowIcons.HPlusMobiledata
        ),
        SubredditInfoItem(
            RainbowStrings.Language,
            RainbowStrings.English,
            RainbowIcons.Language
        ),
    )