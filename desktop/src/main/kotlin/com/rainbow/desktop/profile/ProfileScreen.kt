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
import com.rainbow.desktop.post.SortingItem
import com.rainbow.desktop.post.posts
import com.rainbow.desktop.utils.*
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

    DisposableEffect(overviewItemsState.getOrDefault(emptyList()).isEmpty()) {
        val item = overviewItemsState.getOrNull()?.firstOrNull()
        if (item != null) {
            onNavigateDetailsScreen(DetailsScreen.Post(item.postId))
        }
        onDispose {
            onNavigateDetailsScreen(DetailsScreen.None)
        }
    }

    DisposableEffect(savedItemsState.getOrDefault(emptyList()).isEmpty()) {
        val item = savedItemsState.getOrNull()?.firstOrNull()
        if (item != null) {
            onNavigateDetailsScreen(DetailsScreen.Post(item.postId))
        }
        onDispose {
            onNavigateDetailsScreen(DetailsScreen.None)
        }
    }

    DisposableEffect(submittedPostsState.getOrDefault(emptyList()).isEmpty()) {
        val post = submittedPostsState.getOrNull()?.firstOrNull()
        if (post != null) {
            onNavigateDetailsScreen(DetailsScreen.Post(post.id))
        }
        onDispose {
            onNavigateDetailsScreen(DetailsScreen.None)
        }
    }

    DisposableEffect(upvotedPostsState.getOrDefault(emptyList()).isEmpty()) {
        val post = upvotedPostsState.getOrNull()?.firstOrNull()
        if (post != null) {
            onNavigateDetailsScreen(DetailsScreen.Post(post.id))
        }
        onDispose {
            onNavigateDetailsScreen(DetailsScreen.None)
        }
    }

    DisposableEffect(downvotedPostsState.getOrDefault(emptyList()).isEmpty()) {
        val post = downvotedPostsState.getOrNull()?.firstOrNull()
        if (post != null) {
            onNavigateDetailsScreen(DetailsScreen.Post(post.id))
        }
        onDispose {
            onNavigateDetailsScreen(DetailsScreen.None)
        }
    }

    DisposableEffect(hiddenPostsState.getOrDefault(emptyList()).isEmpty()) {
        val post = hiddenPostsState.getOrNull()?.firstOrNull()
        if (post != null) {
            onNavigateDetailsScreen(DetailsScreen.Post(post.id))
        }
        onDispose {
            onNavigateDetailsScreen(DetailsScreen.None)
        }
    }

    DisposableEffect(commentsState.getOrDefault(emptyList()).isEmpty()) {
        val comment = commentsState.getOrNull()?.firstOrNull()
        if (comment != null) {
            onNavigateDetailsScreen(DetailsScreen.Post(comment.postId))
        }
        onDispose {
            onNavigateDetailsScreen(DetailsScreen.None)
        }
    }

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
                    SortingItem(
                        overviewItemsSorting,
                        overviewItemsTimeSorting,
                        stateHolder.overviewItemsStateHolder::setSorting,
                        stateHolder.overviewItemsStateHolder::setTimeSorting,
                    )
                }

                items(
                    overviewItemsState,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen,
                    onAwardsClick = {},
                    onShowSnackbar,
                    stateHolder.overviewItemsStateHolder::setLastItem,
                )
            }

            ProfileTab.Saved -> {
                item {
                    SortingItem(
                        savedItemsSorting,
                        savedItemsTimeSorting,
                        stateHolder.savedItemsStateHolder::setSorting,
                        stateHolder.savedItemsStateHolder::setTimeSorting,
                    )
                }

                items(
                    savedItemsState,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen,
                    onAwardsClick = {},
                    onShowSnackbar,
                    stateHolder.savedItemsStateHolder::setLastItem,
                )
            }

            ProfileTab.Comments -> {
                item {
                    SortingItem(
                        commentsSorting,
                        commentsTimeSorting,
                        stateHolder.commentsStateHolder::setSorting,
                        stateHolder.commentsStateHolder::setTimeSorting,
                    )
                }

                comments(
                    commentsState,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen,
                    stateHolder.commentsStateHolder::setLastItem,
                )
            }

            ProfileTab.Submitted -> {
                item {
                    SortingItem(
                        submittedPostsSorting,
                        submittedPostsTimeSorting,
                        stateHolder.submittedPostsStateHolder::setSorting,
                        stateHolder.submittedPostsStateHolder::setTimeSorting,
                    )
                }

                posts(
                    submittedPostsState,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen,
                    {},
                    onShowSnackbar,
                    stateHolder.submittedPostsStateHolder::setLastItem,
                )
            }

            ProfileTab.Hidden -> {
                item {
                    SortingItem(
                        hiddenPostsSorting,
                        hiddenPostsTimeSorting,
                        stateHolder.hiddenPostsStateHolder::setSorting,
                        stateHolder.hiddenPostsStateHolder::setTimeSorting,
                    )
                }

                posts(
                    hiddenPostsState,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen,
                    {},
                    onShowSnackbar,
                    stateHolder.hiddenPostsStateHolder::setLastItem,
                )
            }

            ProfileTab.Upvoted -> {
                item {
                    SortingItem(
                        upvotedPostsSorting,
                        upvotedPostsTimeSorting,
                        stateHolder.upvotedPostsStateHolder::setSorting,
                        stateHolder.upvotedPostsStateHolder::setTimeSorting,
                    )
                }

                posts(
                    upvotedPostsState,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen,
                    {},
                    onShowSnackbar,
                    stateHolder.upvotedPostsStateHolder::setLastItem,
                )
            }

            ProfileTab.Downvoted -> {
                item {
                    SortingItem(
                        downvotedPostsSorting,
                        downvotedPostsTimeSorting,
                        stateHolder.downvotedPostsStateHolder::setSorting,
                        stateHolder.downvotedPostsStateHolder::setTimeSorting,
                    )
                }

                posts(
                    downvotedPostsState,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen,
                    {},
                    onShowSnackbar,
                    stateHolder.downvotedPostsStateHolder::setLastItem,
                )
            }
        }
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
            ScreenHeaderItem(user.bannerImageUrl.toString(), user.imageUrl.toString(), user.name)
            HeaderDescription(
                user,
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