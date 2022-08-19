package com.rainbow.desktop.search

//import com.rainbow.desktop.components.LazyGrid
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.components.ScrollableEnumTabRow
import com.rainbow.desktop.navigation.ContentScreen
import com.rainbow.desktop.navigation.Screen

@OptIn(ExperimentalFoundationApi::class)
@Composable
inline fun SearchScreen(
    searchTerm: String,
    crossinline onNavigate: (Screen) -> Unit,
    crossinline onNavigateContentScreen: (ContentScreen) -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val model = remember(searchTerm) { SearchScreenStateHolder.getOrCreateInstance(searchTerm) }
    val selectedTab by model.selectedTab.collectAsState()
//    val subredditsState by model.subredditListModel.items.collectAsState()
//    val postsState by model.postListModel.items.collectAsState()
//    val usersState by model.userListModel.items.collectAsState()
//    val postLayout by model.postListModel.postLayout.collectAsState()
    Column(
        if (selectedTab == SearchTab.Posts) modifier else Modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ScrollableEnumTabRow(
            selectedTab,
            onTabClick = { model.selectTab(it) }
        )

//        when (selectedTab) {
//            SearchTab.Subreddits -> subredditsState.composed(onShowSnackbar) { subreddits ->
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
//            }

//            SearchTab.Posts -> LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
//                posts(
//                    postsState,
//                    onNavigate,
//                    onNavigateContentScreen,
//                    {},
//                    onShowSnackbar,
//                    {}
//                )
//            }

//            SearchTab.Users -> usersState.composed(onShowSnackbar) { users ->
//                LazyGrid {
//                    items(users) { user ->
//                        UserItem(user, onClick = { onUserNameClick(it.name) }, onShowSnackbar)
//                    }
//                }
//            }
//        }
    }
}