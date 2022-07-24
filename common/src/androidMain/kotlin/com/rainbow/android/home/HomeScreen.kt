package com.rainbow.android.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.android.comment.comments
import com.rainbow.common.components.EnumTabRow
import com.rainbow.common.home.HomeScreenModel
import com.rainbow.common.home.HomeTab
import com.rainbow.common.model.ListModel
import com.rainbow.android.post.posts
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Post

@Composable
inline fun HomeScreen(
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    crossinline onPostClick: (Post) -> Unit,
    noinline onPostUpdate: (Post) -> Unit,
    crossinline onPostMenuClick: () -> Unit,
    crossinline onCommentClick: (Comment) -> Unit,
    noinline onCommentUpdate: (Comment) -> Unit,
    crossinline onCommentMenuClick: () -> Unit,
    crossinline onShowAwards: (Post) -> Unit,
    crossinline setListModel: (ListModel<*>) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedTab by HomeScreenModel.selectedTab.collectAsState()
    val posts by HomeScreenModel.postListModel.items.collectAsState()
    val comments by HomeScreenModel.commentListModel.items.collectAsState()
    LazyColumn(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {

        item {
            EnumTabRow(selectedTab, HomeScreenModel::selectTab)
        }

        when (selectedTab) {
            HomeTab.Posts -> posts(
                posts,
                onUserNameClick,
                onSubredditNameClick,
                onPostClick,
                onPostUpdate,
                onPostMenuClick,
                onShowAwards
            )
            HomeTab.Comments -> comments(
                comments,
                onUserNameClick,
                onSubredditNameClick,
                onCommentClick,
                onCommentUpdate,
            )
        }
    }
}