package com.rainbow.app.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.rainbow.app.components.DefaultTabRow
import com.rainbow.app.post.posts
import com.rainbow.app.subreddit.Subreddits
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.composed
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.PostLayout
import com.rainbow.domain.models.Subreddit
import com.rainbow.domain.models.SubredditsSearchSorting
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

enum class SearchTab {
    Subreddits, Posts, Users;

    companion object {
        val Default = Subreddits
    }
}

@Composable
fun SearchScreen(
    searchTerm: String,
    focusRequester: FocusRequester,
    onPostClick: (Post) -> Unit,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(SearchTab.Default) }
    val subredditsState by produceState<UIState<List<Subreddit>>>(UIState.Loading, selectedTab) {
        if (selectedTab == SearchTab.Subreddits)
            Repos.Subreddit.searchSubreddit(searchTerm, SubredditsSearchSorting.Activity)
                .map { it.toUIState() }
                .collect { value = it }
    }
    val postLayout by Repos.Settings.postLayout.collectAsState(PostLayout.Card)
    val postsState by produceState<UIState<List<Post>>>(UIState.Loading, searchTerm, selectedTab) {
        if (selectedTab == SearchTab.Posts)
            Repos.Post.searchPosts(searchTerm)
                .map { it.toUIState() }
                .collect { value = it }
    }
    Column(
        if (selectedTab == SearchTab.Posts) modifier else Modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DefaultTabRow(
            selectedTab,
            onTabClick = { selectedTab = it }
        )

        when (selectedTab) {
            SearchTab.Subreddits -> subredditsState.composed {
                Subreddits(it, { onSubredditNameClick(it.name) }, onLoadMore = {})
            }
            SearchTab.Posts -> LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                posts(
                    postsState,
                    postLayout,
                    focusRequester,
                    onPostClick,
                    onUserNameClick,
                    onSubredditNameClick,
                    onShowSnackbar,
                    onLoadMore = {}
                )
            }
            SearchTab.Users -> TODO()
        }
    }
}