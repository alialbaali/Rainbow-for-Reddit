package com.rainbow.desktop.comment

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Modifier
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.utils.PagingEffect
import com.rainbow.domain.models.Comment

fun LazyListScope.comments(
    comments: List<Comment>,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    onLoadMore: (Comment) -> Unit,
) {
    itemsIndexed(comments) { index, comment ->
        CommentItem(
            comment,
            onClick = { onNavigateDetailsScreen(DetailsScreen.Post(comment.postId)) },
            onUserNameClick = { userName -> onNavigateMainScreen(MainScreen.User(userName)) },
            onSubredditNameClick = { subredditName -> onNavigateMainScreen(MainScreen.Subreddit(subredditName)) },
            onShowSnackbar,
            modifier = Modifier.fillParentMaxWidth(),
        )
        PagingEffect(index, comments.lastIndex) {
            onLoadMore(comment)
        }
    }
}