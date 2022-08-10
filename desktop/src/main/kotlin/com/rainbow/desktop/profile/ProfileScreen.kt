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
import com.rainbow.desktop.navigation.ContentScreen
import com.rainbow.desktop.navigation.Screen
import com.rainbow.desktop.post.posts
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.composed
import com.rainbow.desktop.utils.defaultPadding
import com.rainbow.desktop.utils.defaultSurfaceShape
import com.rainbow.domain.models.User

@Composable
fun ProfileScreen(
    onNavigate: (Screen) -> Unit,
    onNavigateContentScreen: (ContentScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedTab by ProfileScreenStateHolder.selectedTab.collectAsState(ProfileTab.Overview)
    val userState by ProfileScreenStateHolder.currentUser.collectAsState()
    val overViewItemsState by ProfileScreenStateHolder.overViewItemListModel.items.collectAsState()
    val savedItemsState by ProfileScreenStateHolder.savedItemListModel.items.collectAsState()
    val submittedPostsState by ProfileScreenStateHolder.submittedPostListModel.items.collectAsState()
    val hiddenPostsState by ProfileScreenStateHolder.hiddenPostListModel.items.collectAsState()
    val upvotedPostsState by ProfileScreenStateHolder.upvotedPostListModel.items.collectAsState()
    val downvotedPostsState by ProfileScreenStateHolder.downvotedPostListModel.items.collectAsState()
    val commentsState by ProfileScreenStateHolder.commentListModel.items.collectAsState()
    val postLayout by ProfileScreenStateHolder.postLayout.collectAsState()
    userState.composed(onShowSnackbar, modifier) { user ->
        RainbowLazyColumn(modifier) {
            item { Header(user) }
            item {
                ScrollableEnumTabRow(
                    selectedTab = selectedTab,
                    onTabClick = { ProfileScreenStateHolder.selectTab(it) },
                )
            }
            when (selectedTab) {
                ProfileTab.Overview -> items(
                    overViewItemsState,
                    onNavigate,
                    onNavigateContentScreen,
                    { },
                    {},
                    {},
                    {},
                )

                ProfileTab.Saved -> items(
                    savedItemsState,
                    onNavigate,
                    onNavigateContentScreen,
                    { },
                    {},
                    {},
                    {},
                )

                ProfileTab.Comments -> comments(
                    commentsState,
                    onNavigate,
                    onNavigateContentScreen,
                    {},
                )

                ProfileTab.Submitted -> posts(
                    submittedPostsState,
                    onNavigate,
                    onNavigateContentScreen,
                    {},
                    {},
                    onShowSnackbar,
//                    onPostClick
                )

                ProfileTab.Hidden -> posts(
                    hiddenPostsState,
                    onNavigate,
                    onNavigateContentScreen,
                    {},
                    {},
                    onShowSnackbar,
//                    onPostClick
                )

                ProfileTab.Upvoted -> posts(
                    upvotedPostsState,
                    onNavigate,
                    onNavigateContentScreen,
                    {},
                    {},
                    onShowSnackbar,
//                    onPostClick
                )

                ProfileTab.Downvoted -> posts(
                    downvotedPostsState,
                    onNavigate,
                    onNavigateContentScreen,
                    {},
                    {},
                    onShowSnackbar,
//                    onPostClick
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