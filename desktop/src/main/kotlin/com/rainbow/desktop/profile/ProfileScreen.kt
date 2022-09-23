package com.rainbow.desktop.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.desktop.comment.comments
import com.rainbow.desktop.components.*
import com.rainbow.desktop.item.items
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.post.posts
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.defaultPadding
import com.rainbow.desktop.utils.fold
import com.rainbow.domain.models.User

@Composable
fun ProfileScreen(
    onNavigateMainScreen: (MainScreen) -> Unit,
    onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val stateHolder = remember { ProfileScreenStateHolder.Instance }
    val selectedTab by stateHolder.selectedTab.collectAsState(ProfileTab.Overview)
    val userState by stateHolder.currentUser.collectAsState()
    val overviewItemsState by stateHolder.overviewItemsStateHolder.items.collectAsState()
    val overviewItemsSorting by stateHolder.overviewItemsStateHolder.sorting.collectAsState()
    val overviewItemsTimeSorting by stateHolder.overviewItemsStateHolder.timeSorting.collectAsState()
    val savedItemsState by stateHolder.savedItemsStateHolder.items.collectAsState()
    val savedItemsSorting by stateHolder.savedItemsStateHolder.sorting.collectAsState()
    val savedItemsTimeSorting by stateHolder.savedItemsStateHolder.timeSorting.collectAsState()
    val submittedPostsState by stateHolder.submittedPostsStateHolder.items.collectAsState()
    val submittedPostsSorting by stateHolder.submittedPostsStateHolder.sorting.collectAsState()
    val submittedPostsTimeSorting by stateHolder.submittedPostsStateHolder.timeSorting.collectAsState()
    val upvotedPostsState by stateHolder.upvotedPostsStateHolder.items.collectAsState()
    val upvotedPostsSorting by stateHolder.upvotedPostsStateHolder.sorting.collectAsState()
    val upvotedPostsTimeSorting by stateHolder.upvotedPostsStateHolder.timeSorting.collectAsState()
    val downvotedPostsState by stateHolder.downvotedPostsStateHolder.items.collectAsState()
    val downvotedPostsSorting by stateHolder.downvotedPostsStateHolder.sorting.collectAsState()
    val downvotedPostsTimeSorting by stateHolder.downvotedPostsStateHolder.timeSorting.collectAsState()
    val hiddenPostsState by stateHolder.hiddenPostsStateHolder.items.collectAsState()
    val hiddenPostsSorting by stateHolder.hiddenPostsStateHolder.sorting.collectAsState()
    val hiddenPostsTimeSorting by stateHolder.hiddenPostsStateHolder.timeSorting.collectAsState()
    val commentsState by stateHolder.commentsStateHolder.items.collectAsState()
    val commentsSorting by stateHolder.commentsStateHolder.sorting.collectAsState()
    val commentsTimeSorting by stateHolder.commentsStateHolder.timeSorting.collectAsState()
    val selectedItemIds by stateHolder.selectedItemIds.collectAsState()
    val postLayout by stateHolder.postLayout.collectAsState()

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
            ProfileTab.Overview -> {
                item {
                    PostSorting(
                        overviewItemsSorting,
                        overviewItemsTimeSorting,
                        stateHolder.overviewItemsStateHolder::setSorting,
                        stateHolder.overviewItemsStateHolder::setTimeSorting,
                    )
                }

                items(
                    overviewItemsState,
                    postLayout,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen = { detailsScreen ->
                        if (detailsScreen is DetailsScreen.Post) {
                            stateHolder.selectItemId(ProfileTab.Overview, detailsScreen.postId)
                        }
                        onNavigateDetailsScreen(detailsScreen)
                    },
                    onAwardsClick = {},
                    onShowSnackbar,
                    stateHolder.overviewItemsStateHolder::setLastItem,
                )
            }

            ProfileTab.Saved -> {
                item {
                    PostSorting(
                        savedItemsSorting,
                        savedItemsTimeSorting,
                        stateHolder.savedItemsStateHolder::setSorting,
                        stateHolder.savedItemsStateHolder::setTimeSorting,
                    )
                }

                items(
                    savedItemsState,
                    postLayout,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen = { detailsScreen ->
                        if (detailsScreen is DetailsScreen.Post) {
                            stateHolder.selectItemId(ProfileTab.Saved, detailsScreen.postId)
                        }
                        onNavigateDetailsScreen(detailsScreen)
                    },
                    onAwardsClick = {},
                    onShowSnackbar,
                    stateHolder.savedItemsStateHolder::setLastItem,
                )
            }

            ProfileTab.Comments -> {
                item {
                    PostSorting(
                        commentsSorting,
                        commentsTimeSorting,
                        stateHolder.commentsStateHolder::setSorting,
                        stateHolder.commentsStateHolder::setTimeSorting,
                    )
                }

                comments(
                    commentsState,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen = { detailsScreen ->
                        if (detailsScreen is DetailsScreen.Post) {
                            stateHolder.selectItemId(ProfileTab.Comments, detailsScreen.postId)
                        }
                        onNavigateDetailsScreen(detailsScreen)
                    },
                    onShowSnackbar,
                    stateHolder.commentsStateHolder::setLastItem,
                )
            }

            ProfileTab.Submitted -> {
                item {
                    PostSorting(
                        submittedPostsSorting,
                        submittedPostsTimeSorting,
                        stateHolder.submittedPostsStateHolder::setSorting,
                        stateHolder.submittedPostsStateHolder::setTimeSorting,
                    )
                }

                posts(
                    submittedPostsState,
                    postLayout,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen = { detailsScreen ->
                        if (detailsScreen is DetailsScreen.Post) {
                            stateHolder.selectItemId(ProfileTab.Submitted, detailsScreen.postId)
                        }
                        onNavigateDetailsScreen(detailsScreen)
                    },
                    {},
                    onShowSnackbar,
                    stateHolder.submittedPostsStateHolder::setLastItem,
                )
            }

            ProfileTab.Hidden -> {
                item {
                    PostSorting(
                        hiddenPostsSorting,
                        hiddenPostsTimeSorting,
                        stateHolder.hiddenPostsStateHolder::setSorting,
                        stateHolder.hiddenPostsStateHolder::setTimeSorting,
                    )
                }

                posts(
                    hiddenPostsState,
                    postLayout,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen = { detailsScreen ->
                        if (detailsScreen is DetailsScreen.Post) {
                            stateHolder.selectItemId(ProfileTab.Hidden, detailsScreen.postId)
                        }
                        onNavigateDetailsScreen(detailsScreen)
                    },
                    {},
                    onShowSnackbar,
                    stateHolder.hiddenPostsStateHolder::setLastItem,
                )
            }

            ProfileTab.Upvoted -> {
                item {
                    PostSorting(
                        upvotedPostsSorting,
                        upvotedPostsTimeSorting,
                        stateHolder.upvotedPostsStateHolder::setSorting,
                        stateHolder.upvotedPostsStateHolder::setTimeSorting,
                    )
                }

                posts(
                    upvotedPostsState,
                    postLayout,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen = { detailsScreen ->
                        if (detailsScreen is DetailsScreen.Post) {
                            stateHolder.selectItemId(ProfileTab.Upvoted, detailsScreen.postId)
                        }
                        onNavigateDetailsScreen(detailsScreen)
                    },
                    {},
                    onShowSnackbar,
                    stateHolder.upvotedPostsStateHolder::setLastItem,
                )
            }

            ProfileTab.Downvoted -> {
                item {
                    PostSorting(
                        downvotedPostsSorting,
                        downvotedPostsTimeSorting,
                        stateHolder.downvotedPostsStateHolder::setSorting,
                        stateHolder.downvotedPostsStateHolder::setTimeSorting,
                    )
                }

                posts(
                    downvotedPostsState,
                    postLayout,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen = { detailsScreen ->
                        if (detailsScreen is DetailsScreen.Post) {
                            stateHolder.selectItemId(ProfileTab.Downvoted, detailsScreen.postId)
                        }
                        onNavigateDetailsScreen(detailsScreen)
                    },
                    {},
                    onShowSnackbar,
                    stateHolder.downvotedPostsStateHolder::setLastItem,
                )
            }
        }
    }

    DisposableEffect(selectedTab, selectedItemIds) {
        selectedItemIds[selectedTab]?.let { postId ->
            onNavigateDetailsScreen(DetailsScreen.Post(postId))
        }
        onDispose {}
    }
}

@Composable
fun Header(user: User, modifier: Modifier = Modifier) {
    Surface(
        modifier
            .heightIn(min = 350.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            ScreenHeader(user.bannerImageUrl.toString(), user.imageUrl.toString(), user.name)
            ScreenHeaderDescription(
                user.description,
                Modifier
                    .fillMaxWidth()
                    .defaultPadding(start = 232.dp)
            )
            Row(Modifier.fillMaxWidth().defaultPadding(start = 232.dp)) {
                Column(Modifier.weight(1F)) {
                    Text(
                        RainbowStrings.PostKarma,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray,
                        fontSize = 16.sp
                    )
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
        }

//        Text(
//            user.creationDate.toJavaLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME),
//            modifier = Modifier
//                .defaultPadding(start = 232.dp),
//        )
    }
}