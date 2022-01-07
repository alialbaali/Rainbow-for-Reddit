package com.rainbow.common.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.rainbow.common.settings.SettingsModel
import com.rainbow.common.utils.OneTimeEffect
import com.rainbow.common.utils.defaultPadding
import com.rainbow.common.utils.defaultSurfaceShape
import com.rainbow.domain.models.MarkPostAsRead
import com.rainbow.domain.models.Post

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
    val isFullHeight by SettingsModel.isPostFullHeight.collectAsState()
    val markPostAsRead by SettingsModel.markPostAsRead.collectAsState()

    OneTimeEffect(markPostAsRead) {
        if (markPostAsRead == MarkPostAsRead.OnScroll)
            PostActionsModel.readPost(post, onUpdate)
    }

    Column(
        modifier
            .defaultSurfaceShape()
            .clickable {
                onClick(post)
                if (markPostAsRead == MarkPostAsRead.OnClick)
                    PostActionsModel.readPost(post, onUpdate)
            }
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
            onClick,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )
    }
}