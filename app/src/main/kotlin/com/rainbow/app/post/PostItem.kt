package com.rainbow.app.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.app.utils.defaultPadding
import com.rainbow.app.utils.defaultShape
import com.rainbow.data.Repos
import com.rainbow.domain.models.Post

@Composable
inline fun PostItem(
    post: Post,
    crossinline onClick: (Post) -> Unit,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    noinline onCommentsClick: () -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val isFullHeight by Repos.Settings.isFullHeight.collectAsState(false)

    Column(
        modifier
            .defaultShape()
            .clickable { onClick(post) }
            .defaultPadding(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        PostInfo(
            post = post,
            onUserNameClick = onUserNameClick,
            onSubredditNameClick = onSubredditNameClick,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

        PostTitle(
            title = post.title,
            isRead = post.isRead,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

        PostContent(
            post = post,
            modifier = Modifier
                .then(
                    if (isFullHeight)
                        Modifier.fillMaxHeight()
                    else
                        Modifier.heightIn(max = 500.dp)
                )
                .fillMaxWidth()
        )

        PostActions(
            post,
            onCommentsClick,
            onShowSnackbar,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}