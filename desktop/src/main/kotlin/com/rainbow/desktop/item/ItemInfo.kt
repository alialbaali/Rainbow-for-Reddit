package com.rainbow.desktop.item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.rounded.Cake
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.desktop.components.*
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.domain.models.Award
import com.rainbow.domain.models.Flair
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource
import kotlinx.datetime.Instant

private val SubredditIconSize = 50.dp
private val UserIconSize = 30.dp
private val UserIconOffset = 8.dp
private val UserIconBorderWidth = 3.dp
private val CakeDaySize = 20.dp

@Composable
fun ItemInfo(
    userInfo: UserInfo,
    subredditInfo: SubredditInfo?,
    postInfo: PostInfo?,
    commentInfo: CommentInfo?,
    flair: Flair,
    creationDate: Instant,
    awards: List<Award>,
    modifier: Modifier = Modifier,
) {
    Row(modifier, Arrangement.spacedBy(RainbowTheme.dimensions.medium), Alignment.CenterVertically) {
        ItemInfoImages(userInfo, subredditInfo)
        Column(Modifier.fillMaxWidth()) {
            if (subredditInfo != null) {
                SubredditName(subredditInfo.name, subredditInfo.onClick)
                Spacer(Modifier.height(RainbowTheme.dimensions.extraSmall))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (commentInfo == null) {
                    UserName(userInfo.name, userInfo.onClick)
                } else {
                    val color = if (subredditInfo == null)
                        MaterialTheme.colorScheme.onSurface
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                    CommentUserName(userInfo.name, commentInfo.isOP, userInfo.onClick, color = color)
                }
                if (userInfo.isCakeDay) {
                    Dot()
                    Icon(
                        RainbowIcons.Cake,
                        RainbowStrings.CakeDay,
                        Modifier.size(CakeDaySize),
                        MaterialTheme.colorScheme.secondary
                    )
                }
                if (flair.types.isNotEmpty()) {
                    Dot()
                    FlairItem(flair, FlairStyle.Compact)
                }
                Dot()
                CreationDate(creationDate)
                if (postInfo != null && postInfo.isNSFW) {
                    Dot()
                    Text(RainbowStrings.NSFW, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Red)
                }
                if (awards.isNotEmpty()) {
                    Dot()
                    Awards(awards)
                }
            }
        }
    }
}

@Composable
private fun ItemInfoImages(userInfo: UserInfo, subredditInfo: SubredditInfo?, modifier: Modifier = Modifier) {
    val subredditImageResource = lazyPainterResource(subredditInfo?.imageUrl ?: "")
    val userImageResource = lazyPainterResource(userInfo.imageUrl ?: "")
    val isSubredditIconEnabled = subredditInfo != null
    val userIconAlignment = if (isSubredditIconEnabled) Alignment.BottomEnd else Alignment.Center

    Box(modifier) {
        if (subredditInfo != null) {
            KamelImage(
                subredditImageResource,
                subredditInfo.imageUrl,
                Modifier
                    .subredditIcon { subredditInfo.onClick(subredditInfo.name) }
                    .align(Alignment.Center),
                onFailure = {
                    Box(
                        modifier = Modifier
                            .subredditIcon { subredditInfo.onClick(subredditInfo.name) }
                            .align(Alignment.Center),
                        contentAlignment = Alignment.Center
                    ) {
                        TextBox(
                            subredditInfo.name,
                            fontSize = 30.sp,
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                },
                onLoading = {
                    RainbowProgressIndicator(
                        Modifier
                            .subredditIcon { subredditInfo.onClick(subredditInfo.name) }
                            .align(Alignment.Center)
                    )
                }
            )
        }

        KamelImage(
            userImageResource,
            userInfo.name,
            Modifier
                .userIcon(isSubredditIconEnabled) { userInfo.onClick(userInfo.name) }
                .align(userIconAlignment),
            onFailure = {
                Box(
                    modifier = Modifier
                        .userIcon(isSubredditIconEnabled) { userInfo.onClick(userInfo.name) }
                        .align(userIconAlignment),
                    contentAlignment = Alignment.Center
                ) {
                    TextBox(
                        userInfo.name,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            },
            onLoading = {
                RainbowProgressIndicator(
                    Modifier
                        .userIcon(isSubredditIconEnabled) { userInfo.onClick(userInfo.name) }
                        .align(userIconAlignment)
                )
            }
        )
    }
}

private fun Modifier.subredditIcon(onClick: () -> Unit) = composed {
    Modifier
        .clip(MaterialTheme.shapes.small)
        .clickable(onClick = onClick)
        .background(MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.small)
        .size(SubredditIconSize)
}

private fun Modifier.userIcon(isSubredditIconEnabled: Boolean, onClick: () -> Unit) = composed {
    val modifier = if (isSubredditIconEnabled) Modifier
        .offset(UserIconOffset, UserIconOffset)
        .border(UserIconBorderWidth, MaterialTheme.colorScheme.surface, CircleShape)
    else
        Modifier
    modifier
        .clip(CircleShape)
        .clickable(onClick = onClick)
        .background(MaterialTheme.colorScheme.onSurface, CircleShape)
        .size(UserIconSize)
}

data class UserInfo(
    val name: String,
    val imageUrl: String?,
    val isCakeDay: Boolean,
    val onClick: (String) -> Unit,
)

data class SubredditInfo(
    val name: String,
    val imageUrl: String?,
    val onClick: (String) -> Unit
)

data class PostInfo(
    val isNSFW: Boolean,
)

data class CommentInfo(
    val isOP: Boolean,
)