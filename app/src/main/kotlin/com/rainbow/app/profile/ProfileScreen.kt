package com.rainbow.app.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.app.comment.comments
import com.rainbow.app.components.DefaultTabRow
import com.rainbow.app.components.HeaderDescription
import com.rainbow.app.components.HeaderItem
import com.rainbow.app.post.PostModel
import com.rainbow.app.post.PostSorting
import com.rainbow.app.post.posts
import com.rainbow.app.utils.*
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.PostSorting
import com.rainbow.domain.models.User
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

enum class ProfileTab {
    Overview, Submitted, Saved, Hidden, Upvoted, Downvoted, Comments;

    companion object {
        val Default = Submitted
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(
    focusRequester: FocusRequester,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    setPostModel: (PostModel<PostSorting>) -> Unit,
    modifier: Modifier = Modifier,
) {
    setPostModel(ProfileModel.postModel as PostModel<PostSorting>)
    val selectedTab by ProfileModel.tab.collectAsState()
    val postSorting by ProfileModel.postModel.postSorting.collectAsState()
    val timeSorting by ProfileModel.postModel.timeSorting.collectAsState()
    val postLayout by ProfileModel.postModel.postLayout.collectAsState()
    val userState by ProfileModel.currentUser.collectAsState()
    val postsState by ProfileModel.postModel.posts.collectAsState()
    val scrollingState = rememberLazyListState()
    userState.composed(onShowSnackbar, modifier) { user ->
        val commentsState by produceState<UIState<List<Comment>>>(UIState.Loading, selectedTab) {
//            if (selectedTab == ProfileTab.Comments)
//                Repos.Comment.getCurrentUserComments()
//                    .map { it.toUIState() }
//                    .collect { value = it }
        }
        LazyColumn(modifier, scrollingState, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item { Header(user) }
            item {
                DefaultTabRow(
                    selectedTab = selectedTab,
                    onTabClick = { ProfileModel.setTab(it) },
                )
            }
            item {
                PostSorting(
                    postSorting,
                    onSortingUpdate = { ProfileModel.postModel.setPostSorting(it) },
                    timeSorting,
                    onTimeSortingUpdate = { ProfileModel.postModel.setTimeSorting(it) },
                )
            }
            when (selectedTab) {
                ProfileTab.Overview -> {
                }
                ProfileTab.Comments -> comments(commentsState, onUserNameClick, onSubredditNameClick)
                else -> posts(
                    postsState,
                    ProfileModel.postModel,
                    postLayout,
                    focusRequester,
                    onUserNameClick,
                    onSubredditNameClick,
                    onShowSnackbar,
                    onLoadMore = { ProfileModel.postModel.setLastPost(it) }
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