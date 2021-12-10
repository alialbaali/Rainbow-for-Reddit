package com.rainbow.app.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import com.rainbow.app.comment.userComments
import com.rainbow.app.components.RainbowLazyColumn
import com.rainbow.app.model.ListModel
import com.rainbow.app.post.posts
import com.rainbow.app.utils.OneTimeEffect
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.MainPostSorting
import com.rainbow.domain.models.Post

@Composable
inline fun HomeScreen(
    focusRequester: FocusRequester,
    refreshContent: Int,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    noinline onPostUpdate: (Post) -> Unit,
    crossinline onCommentClick: (Comment) -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    crossinline onPostClick: (Post) -> Unit,
    crossinline setListModel: (ListModel<*>) -> Unit,
    modifier: Modifier = Modifier,
) {
    OneTimeEffect(Unit) {
        setListModel(HomeScreenModel.postListModel)
    }
    val postLayout by HomeScreenModel.postListModel.postLayout.collectAsState()
    val postsState by HomeScreenModel.postListModel.items.collectAsState()
    val commentsState by HomeScreenModel.commentListModel.items.collectAsState()
    val sorting by HomeScreenModel.postListModel.postSorting.collectAsState()
    RainbowLazyColumn(modifier) {
        when (sorting) {
            MainPostSorting.Comments -> userComments(
                commentsState,
                onUserNameClick,
                onSubredditNameClick,
                onCommentClick
            )
            else -> posts(
                postsState,
                onPostUpdate,
                postLayout,
                focusRequester,
                onUserNameClick,
                onSubredditNameClick,
                onShowSnackbar,
                setLastPost = {},
                onPostClick,
            )
        }
    }
}