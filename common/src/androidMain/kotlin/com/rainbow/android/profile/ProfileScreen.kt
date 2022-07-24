package com.rainbow.android.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.android.comment.comments
import com.rainbow.android.components.Header
import com.rainbow.common.components.ScrollableEnumTabRow
import com.rainbow.android.item.items
import com.rainbow.common.model.ListModel
import com.rainbow.android.post.posts
import com.rainbow.common.profile.ProfileScreenModel
import com.rainbow.common.profile.ProfileTab
import com.rainbow.common.utils.OneTimeEffect
import com.rainbow.common.utils.composed
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Post

@Composable
inline fun ProfileScreen(
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    crossinline onPostClick: (Post) -> Unit,
    crossinline onCommentClick: (Comment) -> Unit,
    noinline onPostUpdate: (Post) -> Unit,
    noinline onCommentUpdate: (Comment) -> Unit,
    crossinline onPostMenuClick: () -> Unit,
    crossinline onShowAwards: (Post) -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    crossinline setListModel: (ListModel<*>) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedTab by ProfileScreenModel.selectedTab.collectAsState(ProfileTab.Overview)
    val userState by ProfileScreenModel.currentUser.collectAsState()
    val overViewItemsState by ProfileScreenModel.overViewItemListModel.items.collectAsState()
    val savedItemsState by ProfileScreenModel.savedItemListModel.items.collectAsState()
    val submittedPostsState by ProfileScreenModel.submittedPostListModel.items.collectAsState()
    val hiddenPostsState by ProfileScreenModel.hiddenPostListModel.items.collectAsState()
    val upvotedPostsState by ProfileScreenModel.upvotedPostListModel.items.collectAsState()
    val downvotedPostsState by ProfileScreenModel.downvotedPostListModel.items.collectAsState()
    val commentsState by ProfileScreenModel.commentListModel.items.collectAsState()
    val postLayout by ProfileScreenModel.postLayout.collectAsState()

    OneTimeEffect(
        selectedTab,
        overViewItemsState.isLoading,
        submittedPostsState.isLoading,
        savedItemsState.isLoading,
        hiddenPostsState.isLoading,
        upvotedPostsState.isLoading,
        downvotedPostsState.isLoading,
        commentsState.isLoading,
    ) {
        when (selectedTab) {
            ProfileTab.Overview -> setListModel(ProfileScreenModel.overViewItemListModel)
            ProfileTab.Submitted -> setListModel(ProfileScreenModel.submittedPostListModel)
            ProfileTab.Saved -> setListModel(ProfileScreenModel.savedItemListModel)
            ProfileTab.Hidden -> setListModel(ProfileScreenModel.hiddenPostListModel)
            ProfileTab.Upvoted -> setListModel(ProfileScreenModel.upvotedPostListModel)
            ProfileTab.Downvoted -> setListModel(ProfileScreenModel.downvotedPostListModel)
            ProfileTab.Comments -> setListModel(ProfileScreenModel.commentListModel)
        }
    }

    LazyColumn(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            userState.composed(null) { user ->
                Header(user)
            }
        }

        item {
            ScrollableEnumTabRow(selectedTab, ProfileScreenModel::selectTab)
        }

        when (selectedTab) {
            ProfileTab.Overview -> items(
                overViewItemsState,
                onUserNameClick,
                onSubredditNameClick,
                onPostClick,
                onCommentClick,
                onPostUpdate,
                onCommentUpdate,
                onPostMenuClick,
                onShowAwards,
            )
            ProfileTab.Saved -> items(
                savedItemsState,
                onUserNameClick,
                onSubredditNameClick,
                onPostClick,
                onCommentClick,
                onPostUpdate,
                onCommentUpdate,
                onPostMenuClick,
                onShowAwards,
            )
            ProfileTab.Comments -> comments(
                commentsState,
                onUserNameClick,
                onSubredditNameClick,
                onCommentClick,
                onCommentUpdate,
            )
            ProfileTab.Submitted -> posts(
                submittedPostsState,
                onUserNameClick,
                onSubredditNameClick,
                onPostClick,
                onPostUpdate,
                onPostMenuClick,
                onShowAwards,
            )
            ProfileTab.Hidden -> posts(
                hiddenPostsState,
                onUserNameClick,
                onSubredditNameClick,
                onPostClick,
                onPostUpdate,
                onPostMenuClick,
                onShowAwards,
            )
            ProfileTab.Upvoted -> posts(
                upvotedPostsState,
                onUserNameClick,
                onSubredditNameClick,
                onPostClick,
                onPostUpdate,
                onPostMenuClick,
                onShowAwards,
            )
            ProfileTab.Downvoted -> posts(
                downvotedPostsState,
                onUserNameClick,
                onSubredditNameClick,
                onPostClick,
                onPostUpdate,
                onPostMenuClick,
                onShowAwards,
            )
        }
    }
}