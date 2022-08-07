package com.rainbow.desktop.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.rainbow.desktop.comment.comments
import com.rainbow.desktop.components.EnumTabRow
import com.rainbow.desktop.home.HomeScreenModel
import com.rainbow.desktop.home.HomeTab
import com.rainbow.desktop.components.RainbowLazyColumn
import com.rainbow.desktop.model.ListModel
import com.rainbow.desktop.post.posts
import com.rainbow.desktop.utils.OneTimeEffect
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Post

@Composable
inline fun HomeScreen(
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    crossinline onPostClick: (Post) -> Unit,
    crossinline onCommentClick: (Comment) -> Unit,
    noinline onPostUpdate: (Post) -> Unit,
    noinline onCommentUpdate: (Comment) -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
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
                    postLayout,
                    onUserNameClick,
                    onSubredditNameClick,
                    onPostClick,
                    onPostUpdate,
                    {},
                    onShowSnackbar,
                    setLastPost = {},
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