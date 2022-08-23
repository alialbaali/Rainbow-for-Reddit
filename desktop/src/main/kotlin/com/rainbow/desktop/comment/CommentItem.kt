package com.rainbow.desktop.comment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.utils.defaultPadding
import com.rainbow.desktop.utils.defaultSurfaceShape
import com.rainbow.domain.models.Comment

@Composable
inline fun CommentItem(
    comment: Comment,
    crossinline onNavigateMainScreen: (MainScreen) -> Unit,
    crossinline onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .defaultSurfaceShape()
            .clickable { onNavigateDetailsScreen(DetailsScreen.Post(comment.postId)) }
            .defaultPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CommentInfo(
            comment,
            onUserNameClick = { userName -> onNavigateMainScreen(MainScreen.User(userName)) },
            onSubredditNameClick = { subredditName -> onNavigateMainScreen(MainScreen.Subreddit(subredditName)) },
            isSubredditNameEnabled = true
        )
        Text(comment.body, color = MaterialTheme.colorScheme.onBackground)
        CommentActions(comment, false)
    }
}
