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
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.app.award.Awards
import com.rainbow.app.components.*
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.app.utils.defaultPadding
import com.rainbow.app.utils.imageUrl
import com.rainbow.data.Repos
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.Vote
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
        SubredditName(post.subredditName, Modifier.clickable { onSubredditNameClick(post.subredditName) })
        Dot()
        UserName(post.userName, Modifier.clickable { onUserNameClick(post.userName) })
        Dot()
        CreationDate(post.creationDate)
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
    Text(
        buildAnnotatedString {
            append(text.body.trim())
        },
        modifier = modifier,
        style = MaterialTheme.typography.body1
    )
}

@Composable
fun ImagePost(image: Post.Type.Image, modifier: Modifier = Modifier) {
    var isDialogVisible by remember { mutableStateOf(false) }
    val painterResource = lazyPainterResource(image.url)
    val imageShape = MaterialTheme.shapes.medium

    KamelImage(
        painterResource,
        contentDescription = null,
        modifier = modifier
            .clip(imageShape)
            .animateContentSize()
            .clickable { isDialogVisible = true },
        contentScale = ContentScale.Fit,
        onLoading = {
            RainbowProgressIndicator()
        },
        crossfade = true,
        onFailure = {
            throw it
        }
    )

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
            onLoading = { RainbowProgressIndicator() },
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

}

@Composable
fun GifPost(gif: Post.Type.Gif, modifier: Modifier = Modifier) {
    Gif(gif.url, gif.url, modifier)
}

@Composable
fun PostCommands(post: Post, modifier: Modifier = Modifier) {

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

        Text("${post.commentsCount} Comments", style = MaterialTheme.typography.subtitle1)

        Column {
            IconButton(onClick = { isMenuExpanded = true }) {
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

@Composable
fun PostVoteCommands(post: Post, modifier: Modifier = Modifier) {

    val scope = rememberCoroutineScope()

    Row(
        modifier,
        Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        UpvoteButton(
            onClick = {
                when (post.vote) {
                    Vote.Up -> scope.launch {
                        Repos.Post.unvotePost(post.id)
                    }
                    else -> scope.launch {
                        Repos.Post.upvotePost(post.id)
                    }
                }
            },
            tint = when (post.vote) {
                Vote.Up -> MaterialTheme.colors.primary
                else -> MaterialTheme.colors.onBackground
            },
            modifier = Modifier
                .background(
                    when (post.vote) {
                        Vote.Up -> MaterialTheme.colors.primary.copy(0.1F)
                        else -> MaterialTheme.colors.background
                    },
                    CircleShape,
                )
        )

        Spacer(Modifier.width(8.dp))

        val votesCount = when (post.vote) {
            Vote.Up -> post.upvotesCount.inc()
            Vote.Down -> post.upvotesCount.dec()
            Vote.None -> post.upvotesCount
        }

        Text(votesCount.toString())

        Spacer(Modifier.width(8.dp))

        DownvoteButton(
            onClick = {
                when (post.vote) {
                    Vote.Down -> scope.launch {
                        Repos.Post.unvotePost(post.id)
                    }
                    else -> scope.launch {
                        Repos.Post.downvotePost(post.id)
                    }
                }
            },
            tint = when (post.vote) {
                Vote.Down -> MaterialTheme.colors.secondary
                else -> MaterialTheme.colors.onBackground
            },
            modifier = Modifier
                .background(
                    when (post.vote) {
                        Vote.Down -> MaterialTheme.colors.secondary.copy(0.1F)
                        else -> MaterialTheme.colors.background
                    },
                    CircleShape,
                )
        )
    }
}