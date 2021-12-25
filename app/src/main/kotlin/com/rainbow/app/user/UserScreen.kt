package com.rainbow.app.user

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import com.rainbow.app.comment.comments
import com.rainbow.app.components.DefaultTabRow
import com.rainbow.app.components.RainbowLazyColumn
import com.rainbow.app.item.items
import com.rainbow.app.model.ListModel
import com.rainbow.app.post.posts
import com.rainbow.app.profile.Header
import com.rainbow.app.utils.OneTimeEffect
import com.rainbow.app.utils.composed
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Post

enum class UserTab {
    Overview, Submitted, Comments;

    companion object {
        val Default = Overview
    }
}

@Composable
fun UserScreen(
    userName: String,
    onPostUpdate: (Post) -> Unit,
    onPostClick: (Post) -> Unit,
    onCommentClick: (Comment) -> Unit,
    onCommentUpdate: (Comment) -> Unit,
    focusRequester: FocusRequester,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    setListModel: (ListModel<*>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val model = remember(userName) { UserScreenModel.getOrCreateInstance(userName) }
    val userState by model.user.collectAsState()
    val selectedTab by model.selectedTab.collectAsState()
    val postLayout by model.itemListModel.postLayout.collectAsState()
    val itemsState by model.itemListModel.items.collectAsState()
    val postsState by model.postListModel.items.collectAsState()
    val commentsState by model.commentListModel.items.collectAsState()
    OneTimeEffect(selectedTab, itemsState.isLoading, postsState.isLoading, commentsState.isLoading) {
        when (selectedTab) {
            UserTab.Overview -> setListModel(model.itemListModel)
            UserTab.Submitted -> setListModel(model.postListModel)
            UserTab.Comments -> setListModel(model.commentListModel)
        }
    }
    userState.composed(onShowSnackbar) { user ->
        RainbowLazyColumn(modifier) {
            item { Header(user) }
            item {
                DefaultTabRow(
                    selectedTab = selectedTab,
                    onTabClick = { model.selectTab(it) }
                )
            }
            when (selectedTab) {
                UserTab.Overview -> items(
                    itemsState,
                    postLayout,
                    focusRequester,
                    onUserNameClick,
                    onSubredditNameClick,
                    onPostClick,
                    onCommentClick,
                    onPostUpdate,
                    onCommentUpdate,
                    onShowSnackbar,
                )
                UserTab.Submitted -> posts(
                    postsState,
                    onPostUpdate,
                    postLayout,
                    focusRequester,
                    onUserNameClick,
                    onSubredditNameClick,
                    onShowSnackbar,
                    {},
                    onPostClick
                )
                UserTab.Comments -> comments(
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