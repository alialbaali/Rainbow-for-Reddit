package com.rainbow.android

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.rainbow.android.navigation.Screen
import com.rainbow.android.navigation.icon
import com.rainbow.android.navigation.title
import com.rainbow.common.RainbowModel
import com.rainbow.common.post.PostScreenModel
import com.rainbow.common.utils.RainbowIcons
import com.rainbow.common.utils.RainbowStrings
import com.rainbow.common.utils.defaultPadding
import com.rainbow.domain.models.*

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun App() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)
    val postScreenModelType by RainbowModel.postScreenModelType.collectAsState()
    val messageScreenModel by RainbowModel.messageScreenModel.collectAsState()
    val sorting by RainbowModel.sorting.collectAsState()
    val timeSorting by RainbowModel.timeSorting.collectAsState()
    ModalBottomSheetLayout(bottomSheetNavigator) {
        Scaffold(
            topBar = { TopAppBar(navController, sorting, timeSorting) },
            bottomBar = { NavigationBar(navController) },
            floatingActionButton = { Fab(navController) },
        ) {
            NavHost(navController, Screen.NavigationItem.Home.route) {
                content(
                    postScreenModelType,
                    messageScreenModel,
                    sorting,
                    timeSorting,
                    onSortingUpdate = { sorting ->
                        RainbowModel.setSorting(sorting)
                        navController.popBackStack()
                    },
                    onTimeSortingUpdate = {
                        RainbowModel.setTimeSorting(it)
                        navController.popBackStack()
                    },
                    onUserNameClick = { userName -> navController.navigate("${Screen.User.route}/$userName") },
                    onSubredditNameClick = { subredditName -> navController.navigate("${Screen.Subreddit.route}/$subredditName") },
                    onPostClick = { post ->
                        val type = PostScreenModel.Type.PostEntity(post)
                        RainbowModel.selectPost(type)
                        navController.navigate(Screen.Post.route)
                    },
                    onCommentClick = { comment ->
                        val type = PostScreenModel.Type.PostId(comment.id)
                        RainbowModel.selectPost(type)
                        navController.navigate(Screen.Post.route)
                    },
                    onMessageClick = { message ->
                        RainbowModel.selectMessageOrPost(message)
                        navController.navigate(Screen.Message.route)
                    },
                    onSubredditUpdate = RainbowModel::updateSubreddit,
                    onPostUpdate = RainbowModel::updatePost,
                    onCommentUpdate = RainbowModel::updateComment,
                    onNavigate = { sheet -> navController.navigate(sheet.route) },
                    onShowAwards = { post -> navController.navigate("${Screen.Sheet.Awards.route}/${post.id}") },
                    onShowSnackbar = {},
                    setListModel = RainbowModel::setListModel,
                    Modifier.defaultPadding(),
                )
            }
        }
    }
}

private val String.isMainScreen
    get() = Screen.NavigationItem.All.any { it.route == this }

@Composable
private fun TopAppBar(
    navController: NavController,
    sorting: Sorting?,
    timeSorting: TimeSorting?,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
//            val title = when (navBackStackEntry?.destination?.route) {
//                Screen.User.route -> navBackStackEntry?.arguments?.getString("userName")
//                Screen.Post.route -> {
//                    val postId = navBackStackEntry?.arguments?.getString("postId")!!
//                    val model = PostScreenModel.getOrCreateInstance(PostScreenModel.Type.PostId(postId))
//                    model.post.value.getOrNull()?.subredditName ?: ""
//                }
//                Screen.Subreddit.route -> navBackStackEntry?.arguments?.getString("subredditName")
//                Screen.NavigationItem.Home.route -> "Home"
//                Screen.NavigationItem.Subreddits.route -> "Subreddits"
//                Screen.NavigationItem.Messages.route -> "Messages"
//                Screen.NavigationItem.Profile.route -> "Profile"
//                Screen.NavigationItem.Search.route -> "Search"
//                else -> ""
//            } ?: ""
    AnimatedVisibility(navBackStackEntry?.destination?.route?.isMainScreen == true) {
        LargeTopAppBar(
            title = {
                Text(
                    navBackStackEntry?.destination?.route ?: "",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                AnimatedVisibility(navBackStackEntry?.destination?.route?.isMainScreen == false) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(RainbowIcons.ArrowBackIosNew, RainbowStrings.NavigateBack)
                    }
                }
            },
            actions = {
                IconButton(onClick = RainbowModel::refreshContent) {
                    Icon(RainbowIcons.Refresh, RainbowStrings.Refresh)
                }

                AnimatedVisibility(sorting != null) {
                    IconButton(
                        onClick = {
                            when (sorting) {
                                is PostCommentSorting -> navController.navigate(Screen.Sheet.PostCommentSorting.route)
                                is HomePostSorting -> navController.navigate(Screen.Sheet.HomePostSorting.route)
                                is UserPostSorting -> navController.navigate(Screen.Sheet.UserPostSorting.route)
                                is SubredditPostSorting -> navController.navigate(Screen.Sheet.SubredditPostSorting.route)
                                is SearchPostSorting -> navController.navigate(Screen.Sheet.SearchPostSorting.route)
                                null -> {}
                            }
                        }
                    ) {
                        Icon(RainbowIcons.Sort, RainbowIcons.Sort.name)
                    }
                }

                IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                    Icon(RainbowIcons.Settings, RainbowIcons.Settings.name)
                }
            }
        )
    }
}

@Composable
private fun NavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        Screen.NavigationItem.All.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, screen.title) },
                label = { Text(screen.title, fontSize = 12.sp) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                alwaysShowLabel = false
            )
        }
    }
}

@Composable
private fun Fab(navController: NavController) {
    FloatingActionButton(onClick = { navController.navigate(Screen.CreatePost.route) }) {
        Icon(RainbowIcons.Edit, RainbowStrings.CreatePost)
    }
}