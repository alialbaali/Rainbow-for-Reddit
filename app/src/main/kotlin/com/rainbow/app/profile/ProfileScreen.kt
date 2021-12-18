package com.rainbow.app.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.app.comment.comments
import com.rainbow.app.components.DefaultTabRow
import com.rainbow.app.components.HeaderDescription
import com.rainbow.app.components.ScreenHeaderItem
import com.rainbow.app.components.RainbowLazyColumn
import com.rainbow.app.item.items
import com.rainbow.app.model.ListModel
import com.rainbow.app.post.posts
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.composed
import com.rainbow.app.utils.defaultPadding
import com.rainbow.app.utils.defaultSurfaceShape
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.User
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

enum class ProfileTab {
    Overview, Submitted, Saved, Hidden, Upvoted, Downvoted, Comments;

    companion object {
        val Default = Overview
    }
}

@Composable
fun ProfileScreen(
    focusRequester: FocusRequester,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    setListModel: (ListModel<*>) -> Unit,
    onPostUpdate: (Post) -> Unit,
    onPostClick: (Post) -> Unit,
    onCommentClick: (Comment) -> Unit,
    onCommentUpdate: (Comment) -> Unit,
    modifier: Modifier = Modifier,
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
    userState.composed(onShowSnackbar, modifier) { user ->
        RainbowLazyColumn(modifier) {
            item { Header(user) }
            item {
                DefaultTabRow(
                    selectedTab = selectedTab,
                    onTabClick = { ProfileScreenModel.selectTab(it) },
                )
            }
            when (selectedTab) {
                ProfileTab.Overview -> {
                    setListModel(ProfileScreenModel.overViewItemListModel)
                    items(
                        overViewItemsState,
                        postLayout,
                        focusRequester,
                        onUserNameClick,
                        onSubredditNameClick,
                        onPostClick,
                        onCommentClick,
                        onPostUpdate,
                        onCommentUpdate,
                        onShowSnackbar,
                    )
                }
                ProfileTab.Saved -> {
                    setListModel(ProfileScreenModel.savedItemListModel)
                    items(
                        savedItemsState,
                        postLayout,
                        focusRequester,
                        onUserNameClick,
                        onSubredditNameClick,
                        onPostClick,
                        onCommentClick,
                        onPostUpdate,
                        onCommentUpdate,
                        onShowSnackbar,
                    )
                }
                ProfileTab.Comments -> {
                    setListModel(ProfileScreenModel.commentListModel)
                    comments(
                        commentsState,
                        onUserNameClick,
                        onSubredditNameClick,
                        onCommentClick,
                        onCommentUpdate,
                    )
                }
                ProfileTab.Submitted -> {
                    setListModel(ProfileScreenModel.submittedPostListModel)
                    posts(
                        submittedPostsState,
                        onPostUpdate,
                        postLayout,
                        focusRequester,
                        onUserNameClick,
                        onSubredditNameClick,
                        onShowSnackbar,
                        {},
                        onPostClick
                    )
                }
                ProfileTab.Hidden -> {
                    setListModel(ProfileScreenModel.hiddenPostListModel)
                    posts(
                        hiddenPostsState,
                        onPostUpdate,
                        postLayout,
                        focusRequester,
                        onUserNameClick,
                        onSubredditNameClick,
                        onShowSnackbar,
                        {},
                        onPostClick
                    )
                }
                ProfileTab.Upvoted -> {
                    setListModel(ProfileScreenModel.upvotedPostListModel)
                    posts(
                        upvotedPostsState,
                        onPostUpdate,
                        postLayout,
                        focusRequester,
                        onUserNameClick,
                        onSubredditNameClick,
                        onShowSnackbar,
                        {},
                        onPostClick
                    )
                }
                ProfileTab.Downvoted -> {
                    setListModel(ProfileScreenModel.downvotedPostListModel)
                    posts(
                        downvotedPostsState,
                        onPostUpdate,
                        postLayout,
                        focusRequester,
                        onUserNameClick,
                        onSubredditNameClick,
                        onShowSnackbar,
                        {},
                        onPostClick
                    )
                }
            }
        }
    }
}

@Composable
fun Header(user: User, modifier: Modifier = Modifier) {
    Column(
        modifier
            .defaultSurfaceShape()
            .heightIn(min = 350.dp)
            .fillMaxWidth()
    ) {
        ScreenHeaderItem(user.bannerImageUrl.toString(), user.imageUrl.toString(), user.name)
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