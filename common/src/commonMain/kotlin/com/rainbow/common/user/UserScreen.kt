package com.rainbow.common.user

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import com.rainbow.common.comment.comments
import com.rainbow.common.components.ScrollableEnumTabRow
import com.rainbow.common.components.RainbowLazyColumn
import com.rainbow.common.item.items
import com.rainbow.common.model.ListModel
import com.rainbow.common.post.posts
import com.rainbow.common.profile.Header
import com.rainbow.common.utils.OneTimeEffect
import com.rainbow.common.utils.composed
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
    focusRequester: FocusRequester,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onPostClick: (Post) -> Unit,
    onCommentClick: (Comment) -> Unit,
    onPostUpdate: (Post) -> Unit,
    onCommentUpdate: (Comment) -> Unit,
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
                ScrollableEnumTabRow(
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