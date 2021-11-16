package com.rainbow.app.post

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.app.award.Awards
import com.rainbow.app.components.*
import com.rainbow.app.utils.*
import com.rainbow.data.Repos
import com.rainbow.domain.models.Post
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource
import kotlinx.coroutines.launch

@Composable
fun PostTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        modifier = modifier,
        style = MaterialTheme.typography.h5,
    )
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
        SubredditName(post.subredditName, onSubredditNameClick)
        Dot()
        UserName(post.userName, onUserNameClick)
        if (post.userFlairs.isNotEmpty()) {
            Dot()
            Row(
                Modifier
                    .background(Color(post.userFlairBackgroundColor), CircleShape)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                post.userFlairs.forEach { flair ->
                    Flair(flair, post.userFlairTextColor)
                }
            }
        }
        Dot()
        CreationDate(post.creationDate)
        if (post.flairs.isNotEmpty()) {
            Dot()
            Row(
                Modifier
                    .background(Color(post.flairBackgroundColor), CircleShape)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                post.flairs.forEach { flair ->
                    Flair(flair, post.flairTextColor)
                }
            }
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
        is Post.Type.Text -> TextPost(type, modifier)
        is Post.Type.Link -> LinkPost(type, modifier)
        is Post.Type.Gif -> GifPost(type, modifier)
        is Post.Type.Image -> ImagePost(type, modifier)
        is Post.Type.Video -> VideoPost(type, modifier)
    }
}


@Composable
fun TextPost(text: Post.Type.Text, modifier: Modifier = Modifier) {
    var maxLines by remember { mutableStateOf(15) }
    var shouldLimitLines by remember { mutableStateOf(false) }
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            buildAnnotatedString {
                append(text.body.trim())
            },
            modifier = Modifier.animateContentSize(),
            maxLines = if (shouldLimitLines) maxLines else Int.MAX_VALUE,
            style = MaterialTheme.typography.body1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { if (it.lineCount > 15) shouldLimitLines = true }
        )
        if (shouldLimitLines)
            Box(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .defaultShape(shape = CircleShape)
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
fun ImagePost(image: Post.Type.Image, modifier: Modifier = Modifier) {
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
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .background(MaterialTheme.colors.surface),
            contentScale = ContentScale.Fit,
            onLoading = { RainbowProgressIndicator(modifier) },
            crossfade = true,
            onFailure = {
                throw it
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
    }

    ImageWindow(
        painterResource,
        isDialogVisible,
        onCloseRequest = { isDialogVisible = false }
    )
}

@Composable
fun LinkPost(link: Post.Type.Link, modifier: Modifier = Modifier) {
    val painterResource = lazyPainterResource(link.url.imageUrl.toString())
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
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            onLoading = { RainbowProgressIndicator(modifier) },
            crossfade = true,
            onFailure = {
                throw it
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
fun PostActions(
    post: Post,
    onCommentsClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    var isMenuExpanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {

        VoteActions(
            vote = post.vote,
            votesCount = post.upvotesCount.toLong(),
            onUpvote = {
                scope.launch {
                    Repos.Post.upvotePost(post.id)
                }
            },
            onDownvote = {
                scope.launch {
                    Repos.Post.downvotePost(post.id)
                }
            },
            onUnvote = {
                scope.launch {
                    Repos.Post.unvotePost(post.id)
                }
            }
        )

        Row(
            Modifier
                .defaultShape()
                .clickable(onClick = onCommentsClick)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(RainbowIcons.QuestionAnswer, contentDescription = RainbowIcons.QuestionAnswer.name)
            Text(
                text = post.commentsCount.toString(),
                fontSize = 14.sp,
            )
        }

        Column {
            IconButton(onClick = { isMenuExpanded = true }, Modifier.defaultShape()) {
                Icon(RainbowIcons.MoreVert, contentDescription = "More")
            }

            RainbowMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false },
            ) {
                RainbowMenuItem(
                    "Share",
                    RainbowIcons.Share,
                ) {

                }

                RainbowMenuItem(
                    post.userName,
                    RainbowIcons.Person
                ) {

                }

                RainbowMenuItem("Hide", RainbowIcons.VisibilityOff) {

                }
            }
        }
    }
}