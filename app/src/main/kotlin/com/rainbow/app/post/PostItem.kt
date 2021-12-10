package com.rainbow.app.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.rainbow.app.utils.defaultPadding
import com.rainbow.app.utils.defaultSurfaceShape
import com.rainbow.data.Repos
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.PostSorting

@Composable
inline fun PostItem(
    post: Post,
    noinline onUpdate: (Post) -> Unit,
    focusRequester: FocusRequester,
    crossinline onClick: (Post) -> Unit,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    crossinline onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val isFullHeight by Repos.Settings.isFullHeight.collectAsState(false)

    Column(
        modifier
            .defaultSurfaceShape()
            .clickable { onClick(post) }
            .defaultPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
                        Modifier.fillMaxSize()
                    else
                        Modifier.heightIn(max = 500.dp)
                )
                .fillMaxWidth()
        )

        PostActions(
            post,
            onUpdate,
            focusRequester,
            onShowSnackbar,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )
    }
}