package com.rainbow.app.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.rainbow.app.components.DefaultTabRow
import com.rainbow.app.components.LazyGrid
import com.rainbow.app.model.ListModel
import com.rainbow.app.post.posts
import com.rainbow.app.subreddit.SearchSubredditItem
import com.rainbow.app.user.UserItem
import com.rainbow.app.utils.composed
import com.rainbow.domain.models.Post

enum class SearchTab {
    Subreddits, Posts, Users;

    companion object {
        val Default = Subreddits
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(
    searchTerm: String,
    onPostUpdate: (Post) -> Unit,
    onPostClick: (Post) -> Unit,
    focusRequester: FocusRequester,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    setListModel: (ListModel<*>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val model = remember(searchTerm) { SearchScreenModel.getOrCreateInstance(searchTerm) }
    val selectedTab by model.selectedTab.collectAsState()
    val subredditsState by model.subredditListModel.items.collectAsState()
    val postsState by model.postListModel.items.collectAsState()
    val usersState by model.userListModel.items.collectAsState()
    val postLayout by model.postListModel.postLayout.collectAsState()
    Column(
        if (selectedTab == SearchTab.Posts) modifier else Modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DefaultTabRow(
            selectedTab,
            onTabClick = { model.selectTab(it) }
        )

        when (selectedTab) {
            SearchTab.Subreddits -> subredditsState.composed(onShowSnackbar) { subreddits ->
                setListModel(model.subredditListModel)
                LazyGrid {
                    items(subreddits) { subreddit ->
                        SearchSubredditItem(
                            subreddit,
                            onClick = { onSubredditNameClick(it.name) },
                            onShowSnackbar
                        )
                    }
                }
            }
            SearchTab.Posts -> LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                setListModel(model.postListModel)
                posts(
                    postsState,
                    onPostUpdate,
                    postLayout,
                    focusRequester,
                    onUserNameClick,
                    onSubredditNameClick,
                    onShowSnackbar,
                    model.postListModel::setLastItem,
                    onPostClick
                )
            }
            SearchTab.Users -> usersState.composed(onShowSnackbar) { users ->
                setListModel(model.userListModel)
                LazyGrid {
                    items(users) { user ->
                        UserItem(user, onClick = { onUserNameClick(it.name) }, onShowSnackbar)
                    }
                }
            }
        }
    }
}