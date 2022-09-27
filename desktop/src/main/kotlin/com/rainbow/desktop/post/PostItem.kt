package com.rainbow.desktop.post

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.components.FlairItem
import com.rainbow.desktop.components.FlairStyle
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.defaultPadding
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.PostLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompactPostItem(
    post: Post,
    onClick: () -> Unit,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            Modifier
                .defaultPadding()
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(RainbowTheme.dimensions.medium)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(RainbowTheme.dimensions.medium),
            ) {
                Column(
                    modifier = Modifier.weight(1F),
                    verticalArrangement = Arrangement.spacedBy(RainbowTheme.dimensions.medium)
                ) {
                    PostInfo(
                        post,
                        onUserNameClick,
                        onSubredditNameClick,
                    )
                    if (post.flair.types.isNotEmpty()) FlairItem(post.flair, FlairStyle.Default)
                    PostTitle(
                        title = post.title,
                        isRead = post.isRead,
                    )
                    post.body?.let { body ->
                        PostBody(
                            body = body,
                            postLayout = PostLayout.Compact,
                        )
                    }
                }
                PostContent(
                    post = post,
                    postLayout = PostLayout.Compact,
                    modifier = Modifier.size(150.dp).align(Alignment.Bottom),
                )
            }
            PostOptions(post, onShowSnackbar)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardPostItem(
    post: Post,
    onClick: () -> Unit,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            Modifier
                .defaultPadding()
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(RainbowTheme.dimensions.medium)
        ) {
            PostInfo(
                post,
                onUserNameClick,
                onSubredditNameClick,
            )
            if (post.flair.types.isNotEmpty()) FlairItem(post.flair, FlairStyle.Default)
            PostTitle(post.title, post.isRead)
            post.body?.let { body ->
                PostBody(
                    body = body,
                    postLayout = PostLayout.Card,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                )
            }
            PostContent(
                post,
                postLayout = PostLayout.Card,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
            )
            PostOptions(post, onShowSnackbar)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LargePostItem(
    post: Post,
    onClick: () -> Unit,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            Modifier
                .defaultPadding()
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(RainbowTheme.dimensions.medium)
        ) {
            PostInfo(
                post,
                onUserNameClick,
                onSubredditNameClick,
            )
            if (post.flair.types.isNotEmpty()) FlairItem(post.flair, FlairStyle.Default)
            PostTitle(post.title, post.isRead)
            post.body?.let { body ->
                PostBody(
                    body = body,
                    postLayout = PostLayout.Large,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                )
            }
            PostContent(
                post,
                postLayout = PostLayout.Large,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            )
            PostOptions(post, onShowSnackbar)
        }
    }
}