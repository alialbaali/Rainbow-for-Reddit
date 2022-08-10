package com.rainbow.desktop.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.award.ItemAwards
import com.rainbow.desktop.navigation.ContentScreen
import com.rainbow.desktop.navigation.Screen
import com.rainbow.desktop.settings.SettingsStateHolder
import com.rainbow.desktop.utils.defaultPadding
import com.rainbow.domain.models.MarkPostAsRead
import com.rainbow.domain.models.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun PostItem(
    post: Post,
    onNavigate: (Screen) -> Unit,
    crossinline onNavigateContentScreen: (ContentScreen) -> Unit,
    noinline onPostUpdate: (Post) -> Unit,
    crossinline onAwardsClick: () -> Unit,
    crossinline onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val markPostAsRead by SettingsStateHolder.markPostAsRead.collectAsState()
    MarkPostIsReadEffect(post, onPostUpdate, markPostAsRead)
    Surface(
        onClick = {
            onNavigateContentScreen(ContentScreen.PostEntity(post))
            if (markPostAsRead == MarkPostAsRead.OnClick)
                PostActionsStateHolder.readPost(post, onPostUpdate)
        },
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(Modifier.defaultPadding(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            PostInfo(
                post,
                onUserNameClick = { userName -> Screen.User(userName) },
                onSubredditNameClick = { subredditName -> Screen.Subreddit(subredditName) },
            )
            ItemAwards(post.awards, onAwardsClick)
            PostFLairs(post)
            PostTitle(post.title, post.isRead, MaterialTheme.typography.headlineLarge)
            PostContent(post)
            PostActions(
                post,
                onClick = { post -> onNavigateContentScreen(ContentScreen.PostEntity(post)) },
                onPostUpdate
            ) {
                PostActionsMenu(post, onPostUpdate, onShowSnackbar)
            }
        }
    }
}