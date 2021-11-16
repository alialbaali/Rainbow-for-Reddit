package com.rainbow.app.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.app.comment.comments
import com.rainbow.app.components.DefaultTabRow
import com.rainbow.app.components.HeaderDescription
import com.rainbow.app.components.HeaderItem
import com.rainbow.app.post.Sorting
import com.rainbow.app.post.posts
import com.rainbow.app.utils.*
import com.rainbow.data.Repos
import com.rainbow.domain.models.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

private enum class ProfileTab {
    Overview, Submitted, Saved, Hidden, Upvoted, Downvoted, Comments;
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(
    onPostClick: (Post) -> Unit,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onCommentsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedTab by remember { mutableStateOf(ProfileTab.Submitted) }
    var postSorting by remember { mutableStateOf(UserPostSorting.Default) }
    var timeSorting by remember { mutableStateOf(TimeSorting.Default) }
    var lastPost by remember(postSorting, timeSorting) { mutableStateOf<Post?>(null) }
    val scrollingState = rememberLazyListState()
    val postLayout by Repos.Settings.postLayout.collectAsState(PostLayout.Card)
    val state by produceState<UIState<User>>(UIState.Loading) {
        Repos.User.getCurrentUser()
            .toUIState()
            .also { value = it }
    }

    state.composed(modifier) { user ->
        val postsState by producePostsState(user.name, selectedTab, postSorting, timeSorting, lastPost)
        val commentsState by produceState<UIState<List<Comment>>>(UIState.Loading, selectedTab) {
            if (selectedTab == ProfileTab.Comments)
                Repos.Comment.getCurrentUserComments()
                    .map { it.toUIState() }
                    .collect { value = it }
        }
        LazyColumn(modifier, scrollingState, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item { Header(user) }
            item {
                DefaultTabRow(
                    selectedTab = selectedTab,
                    onTabClick = { selectedTab = it },
                )
            }
            item {
                Sorting(
                    postSorting,
                    onSortingUpdate = { postSorting = it },
                    timeSorting,
                    onTimeSortingUpdate = { timeSorting = it },
                )
            }
            when (selectedTab) {
                ProfileTab.Overview -> {
                }
                ProfileTab.Comments -> comments(commentsState, onUserNameClick, onSubredditNameClick)
                else -> posts(
                    postsState,
                    postLayout,
                    onPostClick,
                    onUserNameClick,
                    onSubredditNameClick,
                    onCommentsClick,
                    onLoadMore = { lastPost = it }
                )
            }
        }
        VerticalScrollbar(rememberScrollbarAdapter(scrollingState))
    }
}

@Composable
fun Header(user: User, modifier: Modifier = Modifier) {
    Column(
        modifier
            .defaultShape()
            .heightIn(min = 350.dp)
            .fillMaxWidth()
    ) {
        HeaderItem(user.bannerImageUrl.toString(), user.imageUrl.toString(), user.name)
        HeaderDescription(
            user,
            Modifier
                .fillMaxWidth()
                .defaultPadding(start = 232.dp)
        )
        Row(Modifier.fillMaxWidth().defaultPadding(start = 232.dp)) {
            Column(Modifier.weight(1F)) {
                Text(RainbowStrings.PostKarma, fontWeight = FontWeight.Medium, color = Color.DarkGray, fontSize = 16.sp)
                Text(user.postKarma.toString(), fontSize = 14.sp)
            }
            Column(Modifier.weight(1F)) {
                Text(
                    RainbowStrings.CommentKarma,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray,
                    fontSize = 16.sp
                )
                Text(user.commentKarma.toString(), fontSize = 14.sp)
            }
        }

        Row(Modifier.fillMaxWidth().defaultPadding(start = 232.dp)) {
            Column(Modifier.weight(1F)) {
                Text(
                    RainbowStrings.AwardeeKarma,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray,
                    fontSize = 16.sp
                )
                Text(user.awardeeKarma.toString(), fontSize = 14.sp)
            }
            Column(Modifier.weight(1F)) {
                Text(
                    RainbowStrings.AwarderKarma,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray,
                    fontSize = 16.sp
                )
                Text(user.awarderKarma.toString(), fontSize = 14.sp)
            }
        }

        Text(
            user.creationDate.toJavaLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME),
            modifier = Modifier
                .defaultPadding(start = 232.dp),
        )
    }
}

@Composable
private fun producePostsState(
    userName: String,
    currentTab: ProfileTab,
    postSorting: UserPostSorting,
    timeSorting: TimeSorting,
    lastPost: Post?,
): State<UIState<List<Post>>> {
    return produceState<UIState<List<Post>>>(
        UIState.Loading,
        currentTab,
        postSorting,
        timeSorting,
        lastPost
    ) {
        if (lastPost == null)
            value = UIState.Loading
        when (currentTab) {
            ProfileTab.Submitted -> Repos.Post.getUserSubmittedPosts(
                userName,
                postSorting,
                timeSorting,
                lastPost?.id
            )
            ProfileTab.Saved -> Repos.Post.getUserSavedPosts(
                userName,
                postSorting,
                timeSorting,
                lastPost?.id
            )
            ProfileTab.Hidden -> Repos.Post.getUserHiddenPosts(
                userName,
                postSorting,
                timeSorting,
                lastPost?.id
            )
            ProfileTab.Upvoted -> Repos.Post.getUserUpvotedPosts(
                userName,
                postSorting,
                timeSorting,
                lastPost?.id
            )
            ProfileTab.Downvoted -> Repos.Post.getUserDownvotedPosts(
                userName,
                postSorting,
                timeSorting,
                lastPost?.id
            )
            else -> emptyFlow()
        }.map { it.toUIState() }.collect { value = it }
    }
}