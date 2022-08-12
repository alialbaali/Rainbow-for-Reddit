package com.rainbow.desktop.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.navigation.ContentScreen
import com.rainbow.desktop.navigation.Screen
import com.rainbow.desktop.utils.defaultPadding
import com.rainbow.domain.models.Post

@Composable
inline fun CompactPostItem(
    post: Post,
    crossinline onNavigate: (Screen) -> Unit,
    crossinline onNavigateContentScreen: (ContentScreen) -> Unit,
    crossinline onAwardsClick: () -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isTextPost = remember(post) { post.type is Post.Type.Text }
    Column(
        modifier
            .padding(vertical = 8.dp)
            .clickable { onNavigateContentScreen(ContentScreen.Post(post.id)) }
            .defaultPadding(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        PostInfo(
            post = post,
            onUserNameClick = { userName -> onNavigate(Screen.User(userName)) },
            onSubredditNameClick = { subredditName -> onNavigate(Screen.Subreddit(subredditName)) },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            onAwardsClick = onAwardsClick,
        )
        if (isTextPost) {
            PostTitle(
                title = post.title,
                isRead = post.isRead,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                style = MaterialTheme.typography.headlineLarge
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
                        .weight(1F),
                    style = MaterialTheme.typography.headlineLarge,
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
            onClick = { post -> onNavigateContentScreen(ContentScreen.Post(post.id)) },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {

        }
    }
}