package com.rainbow.desktop.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.desktop.components.*
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.post.posts
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.defaultPadding
import com.rainbow.desktop.utils.defaultSurfaceShape
import com.rainbow.desktop.utils.fold
import com.rainbow.domain.models.User

@Composable
fun ProfileScreen(
    onNavigateMainScreen: (MainScreen) -> Unit,
    onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val stateHolder = remember { ProfileScreenStateHolder.getInstance() }
    val selectedTab by stateHolder.selectedTab.collectAsState(ProfileTab.Overview)
    val userState by stateHolder.currentUser.collectAsState()
//    val overViewItemsState by stateHolder.overViewItemListModel.items.collectAsState()
//    val savedItemsState by stateHolder.savedItemListModel.items.collectAsState()
    val submittedPostsState by stateHolder.submittedPostsStateHolder.items.collectAsState()
    val upvotedPostsState by stateHolder.upvotedPostsStateHolder.items.collectAsState()
    val downvotedPostsState by stateHolder.downvotedPostsStateHolder.items.collectAsState()
    val hiddenPostsState by stateHolder.hiddenPostsStateHolder.items.collectAsState()
//    val commentsState by stateHolder.commentListModel.items.collectAsState()
//    val postLayout by stateHolder.postLayout.collectAsState()

    RainbowLazyColumn(modifier) {
        userState.fold(
            onLoading = {
                item {
                    RainbowProgressIndicator()
                }
            },
            onSuccess = { user ->
                item { Header(user) }
                item {
                    ScrollableEnumTabRow(
                        selectedTab = selectedTab,
                        onTabClick = { stateHolder.selectTab(it) },
                    )
                }
            },
            onFailure = { value, exception ->

            },
            onEmpty = {},
        )
        when (selectedTab) {
//                ProfileTab.Overview -> items(
//                    overViewItemsState,
//                    onNavigateMainScreen,
//                    onNavigateDetailsScreen,
//                    { },
//                    {},
//                )

//                ProfileTab.Saved -> items(
//                    savedItemsState,
//                    onNavigateMainScreen,
//                    onNavigateDetailsScreen,
//                    { },
//                    {},
//                )

//                ProfileTab.Comments -> comments(
//                    commentsState,
//                    onNavigateMainScreen,
//                    onNavigateDetailsScreen,
//                )

            ProfileTab.Submitted -> posts(
                submittedPostsState,
                onNavigateMainScreen,
                onNavigateDetailsScreen,
                {},
                onShowSnackbar,
                stateHolder.submittedPostsStateHolder::setLastItem,
            )

            ProfileTab.Hidden -> posts(
                hiddenPostsState,
                onNavigateMainScreen,
                onNavigateDetailsScreen,
                {},
                onShowSnackbar,
                stateHolder.hiddenPostsStateHolder::setLastItem,
            )

            ProfileTab.Upvoted -> posts(
                upvotedPostsState,
                onNavigateMainScreen,
                onNavigateDetailsScreen,
                {},
                onShowSnackbar,
                stateHolder.upvotedPostsStateHolder::setLastItem,
            )

            ProfileTab.Downvoted -> posts(
                downvotedPostsState,
                onNavigateMainScreen,
                onNavigateDetailsScreen,
                {},
                onShowSnackbar,
                stateHolder.downvotedPostsStateHolder::setLastItem,
            )

            else -> {}
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