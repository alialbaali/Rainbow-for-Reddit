package com.rainbow.desktop.post

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.components.FlairItem
import com.rainbow.desktop.components.FlairStyle
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.settings.SettingsStateHolder
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.defaultPadding
import com.rainbow.domain.models.MarkPostAsRead
import com.rainbow.domain.models.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompactPostItem(
    post: Post,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    onAwardsClick: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val markPostAsRead by SettingsStateHolder.markPostAsRead.collectAsState()
    val isTextPost = remember(post) { post.type is Post.Type.Text }
//    MarkPostIsReadEffect(post, markPostAsRead)
    Surface(
        onClick = {
            onNavigateDetailsScreen(DetailsScreen.Post(post.id))
            if (markPostAsRead == MarkPostAsRead.OnClick)
                PostActionsStateHolder.readPost(post)
        },
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            Modifier
                .defaultPadding()
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(RainbowTheme.dimensions.large)
        ) {
            if (isTextPost) {
                PostInfo(
                    post,
                    onUserNameClick = { userName -> onNavigateMainScreen(MainScreen.User(userName)) },
                    onSubredditNameClick = { subredditName -> onNavigateMainScreen(MainScreen.Subreddit(subredditName)) },
                    onAwardsClick,
                )
                if (post.flair.types.isNotEmpty()) FlairItem(post.flair, FlairStyle.Default)
                PostTitle(
                    title = post.title,
                    isRead = post.isRead,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
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
                    horizontalArrangement = Arrangement.spacedBy(RainbowTheme.dimensions.large),
                ) {
                    Column(
                        modifier = Modifier.weight(1F),
                        verticalArrangement = Arrangement.spacedBy(RainbowTheme.dimensions.large)
                    ) {
                        PostInfo(
                            post,
                            onUserNameClick = { userName -> onNavigateMainScreen(MainScreen.User(userName)) },
                            onSubredditNameClick = { subredditName ->
                                onNavigateMainScreen(
                                    MainScreen.Subreddit(
                                        subredditName
                                    )
                                )
                            },
                            onAwardsClick,
                        )
                        if (post.flair.types.isNotEmpty()) FlairItem(post.flair, FlairStyle.Default)
                        PostTitle(
                            title = post.title,
                            isRead = post.isRead,
                        )
                    }
                    PostContent(
                        post = post,
                        modifier = Modifier.size(100.dp)
                    )
                }
            }
            PostOptions(post, onShowSnackbar)
        }
    }
}