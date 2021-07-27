package com.rainbow.app.post

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.app.award.Awards
import com.rainbow.app.comment.RainbowProgressIndicator
import com.rainbow.app.components.*
import com.rainbow.app.ui.dimensions
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.app.utils.defaultPadding
import com.rainbow.app.utils.displayTime
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.Post.Type
import com.rainbow.domain.models.Vote
import com.rainbow.domain.models.userDisplayName
import io.kamel.image.KamelImage
import io.kamel.image.lazyImageResource


@Composable
fun Post(
    post: Post,
    onUpvote: (String) -> Unit,
    onDownvote: (String) -> Unit,
    onUnvote: (String) -> Unit,
    onShare: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    Card(
        modifier
            .clickable {  }
            .defaultPadding(),
        elevation = 4.dp,
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            Modifier.defaultPadding(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            PostTitle(
                title = post.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )

            PostInfo(
                post = post,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )

            Spacer(Modifier.height(4.dp))

            PostContent(
                post = post,
                modifier = Modifier
                    .heightIn(max = 600.dp)
                    .fillMaxWidth()
            )

            PostCommands(
                post,
                onUpvote = onUpvote,
                onDownvote = onDownvote,
                onUnvote = onUnvote,
                onShare = onShare,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )

        }
    }

}

@Composable
private fun PostTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        modifier = modifier,
        style = MaterialTheme.typography.h5,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PostInfo(
    post: Post,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Text(
            text = post.subredditName,
            Modifier
                .clip(RoundedCornerShape(4.dp))
                .clickable { }
                .padding(MaterialTheme.dimensions.small),
            style = MaterialTheme.typography.subtitle2.copy(
                color = MaterialTheme.colors.onBackground.copy(0.5F),
                fontWeight = FontWeight.SemiBold,
            )
        )

        Dot()

        Text(
            text = post.userName,
            style = MaterialTheme.typography.subtitle2
        )

        Dot()

        Text(
            post.creationDate.displayTime,
            style = MaterialTheme.typography.subtitle2
        )


        if (post.awards.isNotEmpty()) {
            Dot()
            Awards(post.awards)
        }
    }
}

@Composable
private fun PostContent(post: Post, modifier: Modifier = Modifier) {
    when (val type = post.type) {
        is Type.Text -> if (type.body.isNotBlank()) TextPost(type, modifier)
        is Type.Link -> LinkPost(type, modifier)
        is Type.Gif -> GifPost(type, modifier)
        is Type.Image -> ImagePost(type, modifier)
        is Type.Video -> VideoPost(type, modifier)
    }
}


@Composable
private fun TextPost(text: Type.Text, modifier: Modifier = Modifier) {

    Text(
        buildAnnotatedString {
            append(text.body.trim())
        },
        modifier = modifier,
        style = MaterialTheme.typography.body1
    )

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ImagePost(image: Type.Image, modifier: Modifier = Modifier) {
    if (image.url.endsWith("jpg") || image.url.endsWith("png")) {

        val imageResource = lazyImageResource(image.url)

        val imageShape = MaterialTheme.shapes.medium

        KamelImage(
            imageResource,
            contentDescription = null,
            modifier = modifier
                .clip(imageShape)
                .animateContentSize(),
            contentScale = ContentScale.Crop,
            onLoading = {
                RainbowProgressIndicator()
            },
            crossfade = true,
            onFailure = {
                throw it
            }
        )
    }
}

@Composable
private fun LinkPost(link: Type.Link, modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Image(
            ImageBitmap(
                500,
                500,
            ),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.onBackground.copy(0.1F), MaterialTheme.shapes.large),
            alignment = Alignment.Center,
            contentScale = ContentScale.Fit,
            contentDescription = link.url,
        )

        Text(
            link.url,
            Modifier
                .fillMaxWidth()
                .wrapContentWidth()
                .clipToBounds()
                .background(MaterialTheme.colors.onBackground)
                .align(Alignment.BottomCenter),
            MaterialTheme.colors.background,
        )
    }
}

@Composable
private fun VideoPost(video: Type.Video, modifier: Modifier = Modifier) {

}

@Composable
private fun GifPost(gif: Type.Gif, modifier: Modifier = Modifier) {

}

@Composable
private fun PostCommands(
    post: Post,
    onUpvote: (String) -> Unit,
    onDownvote: (String) -> Unit,
    onUnvote: (String) -> Unit,
    onShare: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    var isMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {

        VoteCommands(
            post,
            onUpvote,
            onDownvote,
            onUnvote
        )

        Text("${post.commentsCount} Comments", style = MaterialTheme.typography.subtitle1)

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
                post.userDisplayName,
                RainbowIcons.Person
            ) {

            }

            RainbowMenuItem("Hide", RainbowIcons.VisibilityOff) {

            }
        }
    }
}

@Composable
private fun VoteCommands(
    post: Post,
    onUpvote: (String) -> Unit,
    onDownvote: (String) -> Unit,
    onUnvote: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier,
        Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        UpvoteButton(
            onClick = {
                when (post.vote) {
                    Vote.Up -> onUnvote(post.id)
                    else -> onUpvote(post.id)
                }
            }, tint =
            when (post.vote) {
                Vote.Up -> Color.Blue
                else -> Color.Black
            }
        )

        Spacer(Modifier.width(8.dp))

        Text(
            buildAnnotatedString {
                pushStyle(SpanStyle(fontWeight = FontWeight.SemiBold))
                append("${post.upvotesCount}")
                append('\n')
                pushStyle(
                    SpanStyle(
                        color = MaterialTheme.colors.onBackground.copy(0.5F),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )
                append("${post.upvotesRatio.toString().removeRange(0..1)}%")
            },
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.width(8.dp))

        DownvoteButton(
            onClick = {
                when (post.vote) {
                    Vote.Down -> onUnvote(post.id)
                    else -> onDownvote(post.id)
                }
            }, tint =
            when (post.vote) {
                Vote.Down -> Color.Red
                else -> Color.Black
            }
        )

    }
}