package com.rainbow.common.post

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.common.components.*
import com.rainbow.common.utils.*
import com.rainbow.domain.models.MarkPostAsRead
import com.rainbow.domain.models.Post
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

@Composable
fun PostTitle(title: String, isRead: Boolean, style: TextStyle, modifier: Modifier = Modifier) {
    IsPostReadProvider(isRead) {
        Text(
            text = title,
            modifier = modifier,
            style = style,
        )
    }
}

@Composable
inline fun PostInfo(
    post: Post,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        UserName(post.userName, onUserNameClick)
        Dot()
        SubredditName(post.subredditName, onSubredditNameClick)
        Dot()
        CreationDate(post.creationDate)
        if (post.isNSFW) {
            Dot()
            Text(RainbowStrings.NSFW, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Red)
        }
    }
}

@Composable
fun PostFLairs(post: Post, modifier: Modifier = Modifier) {
    Row(modifier, Arrangement.spacedBy(16.dp), Alignment.CenterVertically) {
        if (post.userFlair.types.isNotEmpty())
            FlairItem(post.userFlair)
        if (post.flair.types.isNotEmpty())
            FlairItem(post.flair)
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
    var maxLines by remember { mutableStateOf(15) }
    var shouldLimitLines by remember { mutableStateOf(false) }
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        IsPostReadProvider(isRead) {
            Text(
                buildAnnotatedString {
                    append(text.body.trim())
                },
                modifier = Modifier.animateContentSize(),
                maxLines = if (shouldLimitLines) maxLines else Int.MAX_VALUE,
                style = MaterialTheme.typography.titleSmall,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { if (it.lineCount > 15) shouldLimitLines = true }
            )
        }
        if (shouldLimitLines)
            Box(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .defaultBackgroundShape(shape = CircleShape)
                    .clickable { if (maxLines == Int.MAX_VALUE) maxLines = 15 else maxLines = Int.MAX_VALUE }
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .wrapContentSize(),
                contentAlignment = Alignment.Center
            ) {
                val imageVector = if (maxLines == Int.MAX_VALUE)
                    RainbowIcons.KeyboardArrowUp
                else
                    RainbowIcons.KeyboardArrowDown
                Icon(imageVector, imageVector.name)
            }
    }
}

@Composable
fun ImagePost(image: Post.Type.Image, isNSFW: Boolean, modifier: Modifier = Modifier) {
    var isDialogVisible by remember { mutableStateOf(false) }
    var imageUrl by remember(image) { mutableStateOf(image.urls.first()) }
    var currentImageIndex by remember(image) { mutableStateOf(0) }
    val painterResource = lazyPainterResource(imageUrl)
    val imageShape = RoundedCornerShape(16.dp)

    Box(
        modifier
            .clip(imageShape)
            .animateContentSize()
            .clickable { isDialogVisible = true },
    ) {
        if (image.urls.count() > 1)
            IconButton(
                onClick = {
                    if (currentImageIndex > 0) {
                        currentImageIndex -= 1
                        imageUrl = image.urls[currentImageIndex]
                    }
                },
                Modifier.align(Alignment.CenterStart),
                enabled = currentImageIndex != 0
            ) {
                Icon(RainbowIcons.ArrowBackIos, RainbowIcons.ArrowBackIos.name)
            }
        KamelImage(
            painterResource,
            contentDescription = null,
            modifier = modifier
                .align(Alignment.Center)
                .background(MaterialTheme.colorScheme.surface),
            contentScale = ContentScale.Fit,
            onLoading = { RainbowProgressIndicator(modifier) },
            animationSpec = tween(),
            onFailure = {

            }
        )
        if (image.urls.count() > 1)
            IconButton(
                onClick = {
                    if (currentImageIndex < image.urls.size - 1) {
                        currentImageIndex += 1
                        imageUrl = image.urls[currentImageIndex]
                    }
                },
                Modifier.align(Alignment.CenterEnd),
                enabled = currentImageIndex != image.urls.size - 1
            ) {
                Icon(RainbowIcons.ArrowForwardIos, RainbowIcons.ArrowForwardIos.name)
            }
        if (isNSFW)
            NSFWBox()
    }

    ImageWindow(
        painterResource,
        isDialogVisible,
        onCloseRequest = { isDialogVisible = false }
    )
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
            contentScale = ContentScale.Crop,
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
//    Video(video.url, modifier)
}

@Composable
fun GifPost(gif: Post.Type.Gif, modifier: Modifier = Modifier) {
//    Gif(gif.url, gif.url, modifier)
}

@Composable
inline fun PostActions(
    post: Post,
    crossinline onClick: (Post) -> Unit,
    noinline onUpdate: (Post) -> Unit,
    modifier: Modifier = Modifier,
    menuContent: @Composable () -> Unit,
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            VoteActions(
                vote = post.vote,
                votesCount = post.votesCount,
                onUpvote = { PostActionsModel.upvotePost(post, onUpdate) },
                onDownvote = { PostActionsModel.downvotePost(post, onUpdate) },
                onUnvote = { PostActionsModel.unvotePost(post, onUpdate) }
            )

            TextIconButton(
                post.commentsCount.toString(),
                RainbowIcons.QuestionAnswer,
                RainbowIcons.QuestionAnswer.name,
                onClick = { onClick(post) },
            )
        }
        menuContent()
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
            PostActionsModel.readPost(post, onPostUpdate)
    }
}