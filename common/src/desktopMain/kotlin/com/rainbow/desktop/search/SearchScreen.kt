package com.rainbow.desktop.search

//import com.rainbow.common.components.LazyGrid
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.common.components.ScrollableEnumTabRow
import com.rainbow.common.model.ListModel
import com.rainbow.common.search.SearchScreenModel
import com.rainbow.common.search.SearchTab
import com.rainbow.common.utils.OneTimeEffect
import com.rainbow.common.utils.composed
import com.rainbow.desktop.post.posts
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.Subreddit

@OptIn(ExperimentalFoundationApi::class)
@Composable
inline fun SearchScreen(
    searchTerm: String,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    crossinline onPostClick: (Post) -> Unit,
    noinline onSubredditUpdate: (Subreddit) -> Unit,
    noinline onPostUpdate: (Post) -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    crossinline setListModel: (ListModel<*>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val model = remember(searchTerm) { SearchScreenModel.getOrCreateInstance(searchTerm) }
    val selectedTab by model.selectedTab.collectAsState()
    val subredditsState by model.subredditListModel.items.collectAsState()
    val postsState by model.postListModel.items.collectAsState()
    val usersState by model.userListModel.items.collectAsState()
    val postLayout by model.postListModel.postLayout.collectAsState()
    OneTimeEffect(selectedTab, subredditsState.isLoading, postsState.isLoading, usersState.isLoading) {
        when (selectedTab) {
            SearchTab.Subreddits -> setListModel(model.subredditListModel)
            SearchTab.Posts -> setListModel(model.postListModel)
            SearchTab.Users -> setListModel(model.userListModel)
        }
    }
    Column(
        if (selectedTab == SearchTab.Posts) modifier else Modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ScrollableEnumTabRow(
            selectedTab,
            onTabClick = { model.selectTab(it) }
        )

        when (selectedTab) {
            SearchTab.Subreddits -> subredditsState.composed(onShowSnackbar) { subreddits ->
//                LazyGrid {
//                    items(subreddits) { subreddit ->
//                        SearchSubredditItem(
//                            subreddit,
//                            onSubredditUpdate,
//                            onClick = { onSubredditNameClick(it.name) },
//                            onShowSnackbar
//                        )
//                    }
//                }
            }
            SearchTab.Posts -> LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                posts(
                    postsState,
                    postLayout,
                    onUserNameClick,
                    onSubredditNameClick,
                    onPostClick,
                    onPostUpdate,
                    {},
                    onShowSnackbar,
                    {}
                )
            }
            SearchTab.Users -> usersState.composed(onShowSnackbar) { users ->
//                LazyGrid {
//                    items(users) { user ->
//                        UserItem(user, onClick = { onUserNameClick(it.name) }, onShowSnackbar)
//                    }
//                }
            }
        }
    }
}