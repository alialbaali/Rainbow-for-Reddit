package com.rainbow.desktop.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.common.award.ItemAwards
import com.rainbow.common.post.*
import com.rainbow.common.settings.SettingsModel
import com.rainbow.common.utils.defaultPadding
import com.rainbow.common.utils.defaultSurfaceShape
import com.rainbow.domain.models.MarkPostAsRead
import com.rainbow.domain.models.Post

@Composable
inline fun PostItem(
    post: Post,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    crossinline onPostClick: (Post) -> Unit,
    noinline onPostUpdate: (Post) -> Unit,
    crossinline onAwardsClick: () -> Unit,
    crossinline onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val markPostAsRead by SettingsModel.markPostAsRead.collectAsState()
    MarkPostIsReadEffect(post, onPostUpdate, markPostAsRead)
    Column(
        modifier
            .defaultSurfaceShape()
            .clickable {
                onPostClick(post)
                if (markPostAsRead == MarkPostAsRead.OnClick)
                    PostActionsModel.readPost(post, onPostUpdate)
            }
            .defaultPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PostInfo(post, onUserNameClick, onSubredditNameClick)
        ItemAwards(post.awards, onAwardsClick)
        PostFLairs(post)
        PostTitle(post.title, post.isRead, MaterialTheme.typography.h5)
        PostContent(post)
        PostActions(post, onPostClick, onPostUpdate) {
            PostActionsMenu(post, onPostUpdate, onShowSnackbar)
        }
    }
}