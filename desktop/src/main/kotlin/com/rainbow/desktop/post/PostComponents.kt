package com.rainbow.desktop.post

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.desktop.components.*
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.OneTimeEffect
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.domain.models.MarkPostAsRead
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.PostLayout
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

private val SubredditIconSize = 50.dp

fun Modifier.subredditIcon(onClick: () -> Unit) = composed {
    Modifier
        .clip(MaterialTheme.shapes.small)
        .clickable(onClick = onClick)
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
    modifier: Modifier = Modifier,
) {
    val resource = lazyPainterResource(post.subredditImageUrl ?: "")
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        KamelImage(
            resource,
            post.subredditName,
            Modifier.subredditIcon { onSubredditNameClick(post.subredditName) },
            onFailure = {
                Box(
                    modifier = Modifier.subredditIcon { onSubredditNameClick(post.subredditName) },
                    contentAlignment = Alignment.Center
                ) {
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
                    Awards(post.awards)
                }
            }
        }
    }
}

@Composable
fun PostContent(post: Post, postLayout: PostLayout, modifier: Modifier = Modifier) {
    when (val type = post.type) {
        is Post.Type.Link -> LinkPost(type, postLayout, modifier)
        is Post.Type.Gif -> GifPost(type, modifier)
        is Post.Type.Image -> ImagePost(type, postLayout, post.isNSFW, modifier)
        is Post.Type.Video -> VideoPost(type, modifier)
        is Post.Type.None -> {}
    }
}

@Composable
fun PostBody(body: String, postLayout: PostLayout, modifier: Modifier = Modifier) {
    val maxLines = remember(postLayout) {
        when (postLayout) {
            PostLayout.Compact -> 5
            PostLayout.Card -> 10
            PostLayout.Large -> 15
        }
    }
    ExpandableText(body, modifier, maxLines)
}

@Composable
fun ImagePost(
    image: Post.Type.Image,
    postLayout: PostLayout,
    isNSFW: Boolean,
    modifier: Modifier = Modifier,
) {
    val painterResource = lazyPainterResource(image.urls.first())
    Box(
        modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.background),
    ) {
        KamelImage(
            painterResource,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = postLayout.toContentScale(),
            onLoading = { RainbowProgressIndicator(Modifier.fillMaxSize()) },
            animationSpec = tween(),
        )

        if (image.urls.size > 1) {
            val outerPadding = key(postLayout) {
                when (postLayout) {
                    PostLayout.Compact -> RainbowTheme.dimensions.small
                    else -> RainbowTheme.dimensions.medium
                }
            }

            val innerPadding = key(postLayout) {
                when (postLayout) {
                    PostLayout.Compact -> RainbowTheme.dimensions.extraSmall
                    else -> RainbowTheme.dimensions.small
                }
            }

            val minSize = remember(postLayout) {
                when (postLayout) {
                    PostLayout.Compact -> 25.dp
                    else -> 40.dp
                }
            }

            val style = key(postLayout) {
                when (postLayout) {
                    PostLayout.Compact -> MaterialTheme.typography.labelMedium
                    else -> MaterialTheme.typography.titleMedium
                }
            }

            Text(
                image.urls.size.toString(),
                modifier = Modifier
                    .padding(outerPadding)
                    .sizeIn(minWidth = minSize, minHeight = minSize)
                    .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.extraSmall)
                    .padding(innerPadding)
                    .align(Alignment.BottomEnd),
                style = style,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun LinkPost(
    link: Post.Type.Link,
    postLayout: PostLayout,
    modifier: Modifier = Modifier
) {
    val painterResource = lazyPainterResource(link.previewUrl)
    val uriHandler = LocalUriHandler.current

    val urlPadding = key(postLayout) {
        when (postLayout) {
            PostLayout.Compact -> PaddingValues(RainbowTheme.dimensions.small)
            else -> PaddingValues(RainbowTheme.dimensions.medium)
        }
    }

    val urlTextStyle = key(postLayout) {
        when (postLayout) {
            PostLayout.Compact -> MaterialTheme.typography.bodyMedium
            else -> MaterialTheme.typography.bodyLarge
        }
    }

    Box(
        modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { uriHandler.openUri(link.url) }
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        KamelImage(
            painterResource,
            contentDescription = link.url,
            modifier = Modifier.fillMaxSize(),
            contentScale = postLayout.toContentScale(),
            onLoading = { RainbowProgressIndicator() },
            onFailure = {}
        )

        Text(
            link.host,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colorScheme.inverseSurface)
                .align(Alignment.BottomCenter)
                .padding(urlPadding),
            color = MaterialTheme.colorScheme.inverseOnSurface,
            style = urlTextStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
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

        CommentsCount(post.commentsCount.toInt())

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

private fun PostLayout.toContentScale(): ContentScale {
    return when (this) {
        PostLayout.Compact -> ContentScale.Crop
        PostLayout.Card -> ContentScale.Crop
        PostLayout.Large -> ContentScale.FillWidth
    }
}