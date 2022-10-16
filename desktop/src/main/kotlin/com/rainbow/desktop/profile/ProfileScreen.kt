package com.rainbow.desktop.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.rounded.Autorenew
import androidx.compose.material.icons.rounded.Cake
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.comment.comments
import com.rainbow.desktop.components.*
import com.rainbow.desktop.item.items
import com.rainbow.desktop.karma.karma
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.post.posts
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.*
import com.rainbow.domain.models.User
import com.rainbow.domain.models.fullUrl

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
    val overviewItems = remember(overviewItemsState) { overviewItemsState.getOrDefault(emptyList()) }
    val overviewItemsSorting by stateHolder.overviewItemsStateHolder.sorting.collectAsState()
    val overviewItemsTimeSorting by stateHolder.overviewItemsStateHolder.timeSorting.collectAsState()
    val savedItemsState by stateHolder.savedItemsStateHolder.items.collectAsState()
    val savedItems = remember(savedItemsState) { savedItemsState.getOrDefault(emptyList()) }
    val savedItemsSorting by stateHolder.savedItemsStateHolder.sorting.collectAsState()
    val savedItemsTimeSorting by stateHolder.savedItemsStateHolder.timeSorting.collectAsState()
    val submittedPostsState by stateHolder.submittedPostsStateHolder.items.collectAsState()
    val submittedPosts = remember(submittedPostsState) { submittedPostsState.getOrDefault(emptyList()) }
    val submittedPostsSorting by stateHolder.submittedPostsStateHolder.sorting.collectAsState()
    val submittedPostsTimeSorting by stateHolder.submittedPostsStateHolder.timeSorting.collectAsState()
    val upvotedPostsState by stateHolder.upvotedPostsStateHolder.items.collectAsState()
    val upvotedPosts = remember(upvotedPostsState) { upvotedPostsState.getOrDefault(emptyList()) }
    val upvotedPostsSorting by stateHolder.upvotedPostsStateHolder.sorting.collectAsState()
    val upvotedPostsTimeSorting by stateHolder.upvotedPostsStateHolder.timeSorting.collectAsState()
    val downvotedPostsState by stateHolder.downvotedPostsStateHolder.items.collectAsState()
    val downvotedPosts = remember(downvotedPostsState) { downvotedPostsState.getOrDefault(emptyList()) }
    val downvotedPostsSorting by stateHolder.downvotedPostsStateHolder.sorting.collectAsState()
    val downvotedPostsTimeSorting by stateHolder.downvotedPostsStateHolder.timeSorting.collectAsState()
    val hiddenPostsState by stateHolder.hiddenPostsStateHolder.items.collectAsState()
    val hiddenPosts = remember(hiddenPostsState) { hiddenPostsState.getOrDefault(emptyList()) }
    val hiddenPostsSorting by stateHolder.hiddenPostsStateHolder.sorting.collectAsState()
    val hiddenPostsTimeSorting by stateHolder.hiddenPostsStateHolder.timeSorting.collectAsState()
    val commentsState by stateHolder.commentsStateHolder.items.collectAsState()
    val comments = remember(commentsState) { commentsState.getOrDefault(emptyList()) }
    val commentsSorting by stateHolder.commentsStateHolder.sorting.collectAsState()
    val commentsTimeSorting by stateHolder.commentsStateHolder.timeSorting.collectAsState()
    val karmaState by stateHolder.karma.collectAsState()
    val karma = remember(karmaState) { karmaState.getOrDefault(emptyList()) }

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
                item { Header(user, onShowSnackbar) }
                item {
                    ScrollableEnumTabRow(
                        selectedTab = selectedTab,
                        onTabClick = { stateHolder.selectTab(it) },
                        icon = { Icon(it.icon, it.name) }
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
                    overviewItems,
                    postLayout,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen = { detailsScreen ->
                        if (detailsScreen is DetailsScreen.Post) {
                            stateHolder.selectItemId(ProfileTab.Overview, detailsScreen.postId)
                        }
                        onNavigateDetailsScreen(detailsScreen)
                    },
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
                    savedItems,
                    postLayout,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen = { detailsScreen ->
                        if (detailsScreen is DetailsScreen.Post) {
                            stateHolder.selectItemId(ProfileTab.Saved, detailsScreen.postId)
                        }
                        onNavigateDetailsScreen(detailsScreen)
                    },
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
                    comments,
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
                    submittedPosts,
                    postLayout,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen = { detailsScreen ->
                        if (detailsScreen is DetailsScreen.Post) {
                            stateHolder.selectItemId(ProfileTab.Submitted, detailsScreen.postId)
                        }
                        onNavigateDetailsScreen(detailsScreen)
                    },
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
                    hiddenPosts,
                    postLayout,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen = { detailsScreen ->
                        if (detailsScreen is DetailsScreen.Post) {
                            stateHolder.selectItemId(ProfileTab.Hidden, detailsScreen.postId)
                        }
                        onNavigateDetailsScreen(detailsScreen)
                    },
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
                    upvotedPosts,
                    postLayout,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen = { detailsScreen ->
                        if (detailsScreen is DetailsScreen.Post) {
                            stateHolder.selectItemId(ProfileTab.Upvoted, detailsScreen.postId)
                        }
                        onNavigateDetailsScreen(detailsScreen)
                    },
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
                    downvotedPosts,
                    postLayout,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen = { detailsScreen ->
                        if (detailsScreen is DetailsScreen.Post) {
                            stateHolder.selectItemId(ProfileTab.Downvoted, detailsScreen.postId)
                        }
                        onNavigateDetailsScreen(detailsScreen)
                    },
                    onShowSnackbar,
                    stateHolder.downvotedPostsStateHolder::setLastItem,
                )
            }

            ProfileTab.Karma -> karma(karma) { subredditName ->
                onNavigateMainScreen(MainScreen.Subreddit(subredditName))
            }
        }

        if (
            overviewItemsState.isLoading || submittedPostsState.isLoading || savedItemsState.isLoading
            || commentsState.isLoading || downvotedPostsState.isLoading || hiddenPostsState.isLoading
            || upvotedPostsState.isLoading
        ) {
            item {
                RainbowProgressIndicator()
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
fun Header(user: User, onShowSnackbar: (String) -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier
            .heightIn(min = ScreenHeaderContentMinHeight)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(Modifier.height(IntrinsicSize.Max)) {
            ScreenHeader(
                user.bannerImageUrl.toString(),
                user.imageUrl.toString(),
                user.name,
            ) {
                PostKarma(user)
                CommentKarma(user)
                ScreenHeaderCreationDate(user.creationDate, RainbowIcons.Cake)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .defaultPadding(start = 232.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                ScreenHeaderDescription(user.description)
                Spacer(Modifier.width(RainbowTheme.dimensions.medium))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Menu(user, onShowSnackbar)
                }
            }
        }
    }
}

private val ProfileTab.icon
    get() = when (this) {
        ProfileTab.Overview -> RainbowIcons.User
        ProfileTab.Submitted -> RainbowIcons.Posts
        ProfileTab.Saved -> RainbowIcons.Saved
        ProfileTab.Hidden -> RainbowIcons.Hidden
        ProfileTab.Upvoted -> RainbowIcons.Upvote
        ProfileTab.Downvoted -> RainbowIcons.Downvote
        ProfileTab.Comments -> RainbowIcons.Comments
        ProfileTab.Karma -> RainbowIcons.Autorenew
    }

@Composable
private fun Menu(
    user: User,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    RainbowDropdownMenuHolder(
        icon = { Icon(RainbowIcons.MoreVert, RainbowStrings.SubredditOptions) },
    ) { handler ->
        OpenInBrowserDropdownMenuItem(user.fullUrl, handler)
        CopyLinkDropdownMenuItem(user.fullUrl, handler, onShowSnackbar)
    }
}

@Composable
private fun PostKarma(user: User, modifier: Modifier = Modifier) {
    val postKarma = remember(user.postKarma) {
        user.postKarma.toInt().format()
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(RainbowTheme.dimensions.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(RainbowIcons.Posts, RainbowStrings.PostsKarma)
        Text(postKarma)
    }
}

@Composable
private fun CommentKarma(user: User, modifier: Modifier = Modifier) {
    val commentKarma = remember(user.commentKarma) {
        user.commentKarma.toInt().format()
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(RainbowTheme.dimensions.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(RainbowIcons.Comments, RainbowStrings.CommentsKarma)
        Text(commentKarma)
    }
}

