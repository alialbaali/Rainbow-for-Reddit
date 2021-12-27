package com.rainbow.app.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.rainbow.app.settings.SettingsModel
import com.rainbow.app.utils.OneTimeEffect
import com.rainbow.app.utils.defaultPadding
import com.rainbow.app.utils.defaultSurfaceShape
import com.rainbow.domain.models.MarkPostAsRead
import com.rainbow.domain.models.Post

@Composable
inline fun CompactPostItem(
    post: Post,
    noinline onUpdate: (Post) -> Unit,
    focusRequester: FocusRequester,
    crossinline onClick: (Post) -> Unit,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isTextPost = remember(post) { post.type is Post.Type.Text }
    val markPostAsRead by SettingsModel.markPostAsRead.collectAsState()

    OneTimeEffect(markPostAsRead) {
        if (markPostAsRead == MarkPostAsRead.OnScroll)
            PostActionsModel.readPost(post, onUpdate)
    }
    Column(
        modifier
            .padding(vertical = 8.dp)
            .defaultSurfaceShape()
            .clickable {
                onClick(post)
                if (markPostAsRead == MarkPostAsRead.OnClick)
                    PostActionsModel.readPost(post, onUpdate)
            }
            .defaultPadding(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        PostInfo(
            post = post,
            onUserNameClick,
            onSubredditNameClick,
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
        )
        if (isTextPost) {
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
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        } else {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                PostTitle(
                    title = post.title,
                    isRead = post.isRead,
                    modifier = Modifier
                        .weight(1F)
                )
                PostContent(
                    post = post,
                    modifier = Modifier
                        .height(100.dp)
                )
            }
        }
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