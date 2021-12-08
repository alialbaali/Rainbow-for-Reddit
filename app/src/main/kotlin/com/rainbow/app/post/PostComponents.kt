package com.rainbow.app.post

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.app.award.Awards
import com.rainbow.app.components.*
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.defaultBackgroundShape
import com.rainbow.app.utils.defaultPadding
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.PostSorting
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

@Composable
fun PostTitle(title: String, isRead: Boolean, modifier: Modifier = Modifier) {
    PostIsReadProvider(isRead) {
        Text(
            text = title,
            modifier = modifier,
            style = MaterialTheme.typography.h5,
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
        if (post.userFlair.types.isNotEmpty()) {
            Dot()
            FlairItem(post.userFlair)
        }
        Dot()
        CreationDate(post.creationDate)
        if (post.flair.types.isNotEmpty()) {
            Dot()
            FlairItem(post.flair)
        }
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

@Composable
fun PostContent(post: Post, modifier: Modifier = Modifier) {
    when (val type = post.type) {
        is Post.Type.Text -> TextPost(type, post.isRead, modifier)
        is Post.Type.Link -> LinkPost(type, modifier)
        is Post.Type.Gif -> GifPost(type, modifier)
        is Post.Type.Image -> ImagePost(type, post.isNSFW, modifier)
        is Post.Type.Video -> VideoPost(type, modifier)
    }
}


@Composable
fun TextPost(text: Post.Type.Text, isRead: Boolean, modifier: Modifier = Modifier) {
    var maxLines by remember { mutableStateOf(15) }
    var shouldLimitLines by remember { mutableStateOf(false) }
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PostIsReadProvider(isRead) {
            Text(
                buildAnnotatedString {
                    append(text.body.trim())
                },
                modifier = Modifier.animateContentSize(),
                maxLines = if (shouldLimitLines) maxLines else Int.MAX_VALUE,
                style = MaterialTheme.typography.subtitle1,
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
    val imageShape = MaterialTheme.shapes.medium

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
                .background(MaterialTheme.colors.surface),
            contentScale = ContentScale.Fit,
            onLoading = { RainbowProgressIndicator(modifier) },
            crossfade = true,
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
    val imageShape = MaterialTheme.shapes.medium
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
            crossfade = true,
            onFailure = {

            }
        )

        Text(
            link.url,
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(imageShape)
                .background(MaterialTheme.colors.onBackground)
                .align(Alignment.BottomCenter)
                .defaultPadding(),
            MaterialTheme.colors.background,
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
fun <T : PostSorting> PostActions(
    post: Post,
    postModel: PostModel<T>,
    focusRequester: FocusRequester,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    var isMenuExpanded by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    val clipboardManager = LocalClipboardManager.current

    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {

        VoteActions(
            vote = post.vote,
            votesCount = post.upvotesCount.toLong(),
            onUpvote = { postModel.upvotePost(post.id) },
            onDownvote = { postModel.downvotePost(post.id) },
            onUnvote = { postModel.unvotePost(post.id) }
        )

        TextIconButton(
            onClick = { focusRequester.requestFocus() },
            Modifier.defaultBackgroundShape()
        ) {
            Icon(RainbowIcons.QuestionAnswer, contentDescription = RainbowIcons.QuestionAnswer.name)
            Text(
                text = post.commentsCount.toString(),
                fontSize = 14.sp,
            )
        }

        Column {
            MenuIconButton(onClick = { isMenuExpanded = true })

            RainbowMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false },
            ) {
                RainbowMenuItem(
                    RainbowStrings.OpenInBrowser,
                    RainbowIcons.OpenInBrowser,
                    onclick = {
                        uriHandler.openUri(post.url)
                        isMenuExpanded = false
                    }
                )

                RainbowMenuItem(
                    RainbowStrings.CopyLink,
                    RainbowIcons.ContentCopy,
                    onclick = {
                        clipboardManager.setText(AnnotatedString(post.url))
                        isMenuExpanded = false
                        onShowSnackbar(RainbowStrings.LinkIsCopied)
                    }
                )

                if (post.isHidden)
                    RainbowMenuItem(
                        RainbowStrings.UnHide,
                        RainbowIcons.Visibility,
                        onclick = {
                            postModel.unHidePost(post.id)
                            isMenuExpanded = false
                            onShowSnackbar(RainbowStrings.PostIsUnHidden)
                        }
                    )
                else
                    RainbowMenuItem(
                        RainbowStrings.Hide,
                        RainbowIcons.VisibilityOff,
                        onclick = {
                            postModel.hidePost(post.id)
                            isMenuExpanded = false
                            onShowSnackbar(RainbowStrings.PostIsHidden)
                        }
                    )
            }
        }
    }
}

@Composable
private fun PostIsReadProvider(isRead: Boolean, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalContentAlpha provides if (isRead) ContentAlpha.medium else ContentAlpha.high) {
        content()
    }
}