package com.rainbow.desktop.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.desktop.comment.comments
import com.rainbow.desktop.components.HeaderDescription
import com.rainbow.desktop.components.RainbowLazyColumn
import com.rainbow.desktop.components.ScreenHeaderItem
import com.rainbow.desktop.components.ScrollableEnumTabRow
import com.rainbow.desktop.item.items
import com.rainbow.desktop.model.ListModel
import com.rainbow.desktop.profile.ProfileScreenModel
import com.rainbow.desktop.profile.ProfileTab
import com.rainbow.desktop.post.posts
import com.rainbow.desktop.utils.*
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.User

@Composable
inline fun ProfileScreen(
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    crossinline onPostClick: (Post) -> Unit,
    crossinline onCommentClick: (Comment) -> Unit,
    noinline onPostUpdate: (Post) -> Unit,
    noinline onCommentUpdate: (Comment) -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    crossinline setListModel: (ListModel<*>) -> Unit,
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
    userState.composed(onShowSnackbar, modifier) { user ->
        RainbowLazyColumn(modifier) {
            item { Header(user) }
            item {
                ScrollableEnumTabRow(
                    selectedTab = selectedTab,
                    onTabClick = { ProfileScreenModel.selectTab(it) },
                )
            }
            when (selectedTab) {
                ProfileTab.Overview -> items(
                    overViewItemsState,
                    postLayout,
                    onUserNameClick,
                    onSubredditNameClick,
                    onPostClick,
                    onCommentClick,
                    onPostUpdate,
                    onCommentUpdate,
                    {},
                    {},
                )
                ProfileTab.Saved -> items(
                    savedItemsState,
                    postLayout,
                    onUserNameClick,
                    onSubredditNameClick,
                    onPostClick,
                    onCommentClick,
                    onPostUpdate,
                    onCommentUpdate,
                    {},
                    {},
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
                    postLayout,
                    onUserNameClick,
                    onSubredditNameClick,
                    onPostClick,
                    onPostUpdate,
                    {},
                    onShowSnackbar,
                    onPostClick
                )
                ProfileTab.Hidden -> posts(
                    hiddenPostsState,
                    postLayout,
                    onUserNameClick,
                    onSubredditNameClick,
                    onPostClick,
                    onPostUpdate,
                    {},
                    onShowSnackbar,
                    onPostClick
                )
                ProfileTab.Upvoted -> posts(
                    upvotedPostsState,
                    postLayout,
                    onUserNameClick,
                    onSubredditNameClick,
                    onPostClick,
                    onPostUpdate,
                    {},
                    onShowSnackbar,
                    onPostClick
                )
                ProfileTab.Downvoted -> posts(
                    downvotedPostsState,
                    postLayout,
                    onUserNameClick,
                    onSubredditNameClick,
                    onPostClick,
                    onPostUpdate,
                    {},
                    onShowSnackbar,
                    {},
                )
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

//        Text(
//            user.creationDate.toJavaLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME),
//            modifier = Modifier
//                .defaultPadding(start = 232.dp),
//        )
    }
}