package com.rainbow.common.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import com.rainbow.common.comment.comments
import com.rainbow.common.components.EnumTabRow
import com.rainbow.common.components.RainbowLazyColumn
import com.rainbow.common.model.ListModel
import com.rainbow.common.post.posts
import com.rainbow.common.utils.OneTimeEffect
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Post

enum class HomeTab {
    Posts, Comments;

    companion object {
        val Default = Posts
    }
}

@Composable
inline fun HomeScreen(
    focusRequester: FocusRequester,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    noinline onPostUpdate: (Post) -> Unit,
    crossinline onCommentClick: (Comment) -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    crossinline onPostClick: (Post) -> Unit,
    noinline onCommentUpdate: (Comment) -> Unit,
    crossinline setListModel: (ListModel<*>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val postLayout by HomeScreenModel.postListModel.postLayout.collectAsState()
    val postsState by HomeScreenModel.postListModel.items.collectAsState()
    val commentsState by HomeScreenModel.commentListModel.items.collectAsState()
    val selectedTab by HomeScreenModel.selectedTab.collectAsState()
    OneTimeEffect(selectedTab, postsState.isLoading, commentsState.isLoading) {
        when (selectedTab) {
            HomeTab.Posts -> setListModel(HomeScreenModel.postListModel)
            HomeTab.Comments -> setListModel(HomeScreenModel.commentListModel)
        }
    }
    RainbowLazyColumn(modifier) {
        item {
            EnumTabRow(
                selectedTab,
                onTabClick = { HomeScreenModel.selectTab(it) }
            )
        }
        when (selectedTab) {
            HomeTab.Posts -> {
                posts(
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
            HomeTab.Comments -> {
                comments(
                    commentsState,
                    onUserNameClick,
                    onSubredditNameClick,
                    onCommentClick,
                    onCommentUpdate,
                )
            }
        }
    }
}