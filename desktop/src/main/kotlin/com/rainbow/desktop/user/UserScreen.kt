package com.rainbow.desktop.user

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.ScrollableEnumTabRow
import com.rainbow.desktop.model.ListModel
import com.rainbow.desktop.utils.OneTimeEffect
import com.rainbow.desktop.utils.composed
import com.rainbow.desktop.comment.comments
import com.rainbow.desktop.item.items
import com.rainbow.desktop.post.posts
import com.rainbow.desktop.profile.Header
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Post

@Composable
fun UserScreen(
    userName: String,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onPostClick: (Post) -> Unit,
    onCommentClick: (Comment) -> Unit,
    onPostUpdate: (Post) -> Unit,
    onCommentUpdate: (Comment) -> Unit,
    onAwardsClick: () -> Unit,
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
        LazyColumn(modifier) {
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
                    onUserNameClick,
                    onSubredditNameClick,
                    onPostClick,
                    onCommentClick,
                    onPostUpdate,
                    onCommentUpdate,
                    onAwardsClick,
                    onShowSnackbar,
                )
                UserTab.Submitted -> posts(
                    postsState,
                    postLayout,
                    onUserNameClick,
                    onSubredditNameClick,
                    onPostClick,
                    onPostUpdate,
                    onAwardsClick,
                    onShowSnackbar,
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