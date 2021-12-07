package com.rainbow.app.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.rainbow.app.components.DefaultTabRow
import com.rainbow.app.post.PostModel
import com.rainbow.app.post.posts
import com.rainbow.app.subreddit.SubredditType
import com.rainbow.app.subreddit.Subreddits
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.composed
import com.rainbow.data.Repos
import com.rainbow.domain.models.PostLayout
import com.rainbow.domain.models.PostSorting
import com.rainbow.domain.models.Subreddit

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
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    setPostModel: (PostModel<PostSorting>) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedTab by remember { mutableStateOf(SearchTab.Default) }
    val subredditsState by produceState<UIState<List<Subreddit>>>(UIState.Loading, selectedTab) {
//        if (selectedTab == SearchTab.Subreddits)
//            Repos.Subreddit.searchSubreddit(searchTerm, SubredditsSearchSorting.Activity)
//                .map { it.toUIState() }
//                .collect { value = it }
    }
    setPostModel(SearchModel.postModel as PostModel<PostSorting>)
    val postLayout by Repos.Settings.postLayout.collectAsState(PostLayout.Card)
    val postsState by SearchModel.postModel.posts.collectAsState()
    Column(
        if (selectedTab == SearchTab.Posts) modifier else Modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DefaultTabRow(
            selectedTab,
            onTabClick = { selectedTab = it }
        )

        when (selectedTab) {
            SearchTab.Subreddits -> subredditsState.composed(onShowSnackbar) {
                Subreddits(it, SubredditType.Search, { onSubredditNameClick(it.name) }, onLoadMore = {}, onShowSnackbar)
            }
            SearchTab.Posts -> LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                posts(
                    postsState,
                    SearchModel.postModel,
                    postLayout,
                    focusRequester,
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