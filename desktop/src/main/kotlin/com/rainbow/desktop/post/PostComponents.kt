package com.rainbow.desktop.post

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.desktop.award.ItemAwards
import com.rainbow.desktop.components.*
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.*
import com.rainbow.domain.models.MarkPostAsRead
import com.rainbow.domain.models.Post
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

private val SubredditIconSize = 48.dp

fun Modifier.subredditIcon() = composed {
    Modifier
        .clip(MaterialTheme.shapes.small)
        .background(MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.small)
        .size(SubredditIconSize)
}

@Composable
fun PostTitle(title: String, isRead: Boolean, modifier: Modifier = Modifier) {
    Text(
        text = title,
        modifier = modifier,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
fun PostInfo(
    post: Post,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onAwardsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val resource = lazyPainterResource(post.subredditImageUrl ?: "")
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        KamelImage(
            resource,
            post.subredditName,
            Modifier.subredditIcon(),
            onFailure = {
                Box(Modifier.subredditIcon(), contentAlignment = Alignment.Center) {
                    TextBox(
                        post.subredditName.first().toString().uppercase(),
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        )
        Column(Modifier.fillMaxWidth()) {
            SubredditName(post.subredditName, onSubredditNameClick)
            Row(verticalAlignment = Alignment.CenterVertically) {
                UserName(post.userName, onUserNameClick)
                if (post.userFlair.types.isNotEmpty()) {
                    Dot()
                    FlairItem(post.userFlair, FlairStyle.Compact)
                }
                Dot()
                CreationDate(post.creationDate)
                if (post.isNSFW) {
                    Dot()
                    Text(RainbowStrings.NSFW, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Red)
                }
                if (post.awards.isNotEmpty()) {
                    Dot()
                    ItemAwards(post.awards, onAwardsClick)
                }
            }
        }
    }
}

@Composable
fun PostContent(post: Post, modifier: Modifier = Modifier) {
    when (val type = post.type) {
        is Post.Type.Text -> TextPost(type, post.isRead, modifier)
        is Post.Type.Link -> LinkPost(type, modifier)
        is Post.Type.Gif -> GifPost(type, modifier)
        is Post.Type.Image -> ImagePost(type, post.isNSFW, modifier)
        is Post.Type.Video -> VideoPost(type, modifier)
        is Post.Type.None -> {}
    }
}


@Composable
fun TextPost(text: Post.Type.Text, isRead: Boolean, modifier: Modifier = Modifier) {
    Text(
        text.body,
        maxLines = 5,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
fun ImagePost(image: Post.Type.Image, isNSFW: Boolean, modifier: Modifier = Modifier) {
    val painterResource = lazyPainterResource(image.urls.first())
    Box(modifier) {
        KamelImage(
            painterResource,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium),
            contentScale = ContentScale.FillWidth,
            onLoading = { RainbowProgressIndicator(Modifier.fillMaxSize()) },
            animationSpec = tween(),
        )

        if (image.urls.size > 1) {
            Text(
                image.urls.size.toString(),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium)
                    .defaultPadding()
                    .align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
fun LinkPost(link: Post.Type.Link, modifier: Modifier = Modifier) {
    val painterResource = lazyPainterResource(link.previewUrl)
    val imageShape = RoundedCornerShape(8.dp)
    val uriHandler = LocalUriHandler.current

    Box(
        modifier
            .clip(imageShape)
            .clickable { uriHandler.openUri(link.url) },
        contentAlignment = Alignment.Center
    ) {
        KamelImage(
            painterResource,
            contentDescription = link.url,
            modifier = modifier,
            contentScale = ContentScale.FillWidth,
            onLoading = { RainbowProgressIndicator(modifier) },
            animationSpec = tween(),
            onFailure = {

            }
        )

        Text(
            link.url,
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(imageShape)
                .background(MaterialTheme.colorScheme.onBackground)
                .align(Alignment.BottomCenter)
                .defaultPadding(),
            MaterialTheme.colorScheme.background,
            fontSize = 16.sp
        )
    }
}

@Composable
fun VideoPost(video: Post.Type.Video, modifier: Modifier = Modifier) {
    // TODO
}

@Composable
fun GifPost(gif: Post.Type.Gif, modifier: Modifier = Modifier) {
    // TODO
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PostOptions(
    post: Post,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        VoteActions(
            vote = post.vote,
            votesCount = post.votesCount,
            onUpvote = { PostActionsStateHolder.upvotePost(post) },
            onDownvote = { PostActionsStateHolder.downvotePost(post) },
            onUnvote = { PostActionsStateHolder.unvotePost(post) }
        )

        Row(
            Modifier
                .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.small)
                .padding(vertical = RainbowTheme.dpDimensions.small, horizontal = RainbowTheme.dpDimensions.medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(RainbowTheme.dpDimensions.medium)
        ) {
            Icon(
                RainbowIcons.Forum,
                RainbowStrings.Comments,
                tint = MaterialTheme.colorScheme.onBackground
            )
            Text(
                post.commentsCount.toInt().format(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        Row(
            Modifier.background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RainbowIconButton(
                onClick = {
                    if (post.isHidden) {
                        PostActionsStateHolder.unHidePost(post)
                    } else {
                        PostActionsStateHolder.hidePost(post)
                    }
                },
                containerColor = MaterialTheme.colorScheme.background,
                hoverContainerColor = MaterialTheme.colorScheme.background,
            ) {
                if (post.isHidden) {
                    Icon(RainbowIcons.Visibility, RainbowStrings.UnHide)
                } else {
                    Icon(RainbowIcons.VisibilityOff, RainbowStrings.Hide)
                }
            }

            RainbowIconToggleButton(
                checked = post.isSaved,
                onCheckedChange = {
                    if (post.isSaved) {
                        PostActionsStateHolder.unSavePost(post)
                    } else {
                        PostActionsStateHolder.savePost(post)
                    }
                },
                checkedContentColor = RainbowTheme.colors.yellow,
                checkedContainerColor = MaterialTheme.colorScheme.background,
                hoverContentColor = RainbowTheme.colors.yellow.copy(0.5F)
            ) {
                AnimatedContent(post.isSaved) { isSaved ->
                    if (isSaved) {
                        Icon(RainbowIcons.Star, RainbowStrings.Unsave)
                    } else {
                        Icon(RainbowIcons.StarBorder, RainbowStrings.Save)
                    }
                }
            }

            RainbowDropdownMenuHolder(
                icon = { Icon(RainbowIcons.MoreVert, RainbowStrings.PostOptions) }
            ) { handler ->
                OpenInBrowserDropdownMenuItem(post.url, handler)
                CopyLinkDropdownMenuItem(post.url, handler, onShowSnackbar)
            }
        }
    }
}

@Composable
private fun IsPostReadProvider(isRead: Boolean, content: @Composable () -> Unit) {
//    CompositionLocalProvider(LocalContentAlpha provides if (isRead) ContentAlpha.medium else ContentAlpha.high) {
//        content()
//    }
}

@Composable
fun MarkPostIsReadEffect(post: Post, onPostUpdate: (Post) -> Unit, markPostAsRead: MarkPostAsRead) {
    OneTimeEffect(markPostAsRead) {
        if (markPostAsRead == MarkPostAsRead.OnScroll)
            PostActionsStateHolder.readPost(post)
    }
}