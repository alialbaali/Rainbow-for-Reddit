package com.rainbow.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.rainbow.common.navigation.Screen
import com.rainbow.common.navigation.icon
import com.rainbow.common.navigation.title
import com.rainbow.common.post.PostScreenModel
import com.rainbow.common.utils.RainbowIcons
import com.rainbow.common.utils.RainbowStrings
import com.rainbow.domain.models.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialNavigationApi::class)
@Composable
internal actual fun App() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)
    val postScreenModelType by RainbowModel.postScreenModelType.collectAsState()
    val messageScreenModel by RainbowModel.messageScreenModel.collectAsState()
    val focusRequester = remember { FocusRequester() }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val sorting by RainbowModel.sorting.collectAsState()
    val timeSorting by RainbowModel.timeSorting.collectAsState()

    ModalBottomSheetLayout(bottomSheetNavigator) {
        Scaffold(
            topBar = {
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
                        title = { Text(navBackStackEntry?.destination?.route ?: "", fontWeight = FontWeight.Bold) },
                        navigationIcon = {
                            AnimatedVisibility(navBackStackEntry?.destination?.route?.isMainScreen == false) {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(RainbowIcons.ArrowBackIosNew, RainbowStrings.NavigateBack)
                                }
                            }
                        },
                        actions = {
                            IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                                Icon(RainbowIcons.Settings, RainbowIcons.Settings.name)
                            }

                            AnimatedVisibility(sorting != null) {
                                IconButton(onClick = {
                                    when (sorting) {
                                        is PostCommentSorting -> navController.navigate(Screen.Sheet.PostCommentSorting.route)
                                        is HomePostSorting -> navController.navigate(Screen.Sheet.HomePostSorting.route)
                                        is UserPostSorting -> navController.navigate(Screen.Sheet.UserPostSorting.route)
                                        is SubredditPostSorting -> navController.navigate(Screen.Sheet.SubredditPostSorting.route)
                                        is SearchPostSorting -> navController.navigate(Screen.Sheet.SearchPostSorting.route)
                                        null -> {}
                                    }
                                }) {
                                    Icon(RainbowIcons.Sort, RainbowIcons.Sort.name)
                                }
                            }
                        }
                    )
                }
            },
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    Screen.NavigationItem.All.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, screen.title) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            },
            floatingActionButton = {
                LargeFloatingActionButton(onClick = {}) {
                    Icon(RainbowIcons.Edit, RainbowStrings.CreatePost)
                }
            },
        ) {
            NavHost(navController, Screen.NavigationItem.Home.route) {
                content(
                    focusRequester,
                    postScreenModelType,
                    messageScreenModel,
                    sorting,
                    timeSorting,
                    onPostSortingUpdate = { sorting ->
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
                    onShowSnackbar = {

                    },
                    setListModel = RainbowModel::setListModel,
                )
            }
        }
    }
}

private val String.isMainScreen
    get() = Screen.NavigationItem.All.any { it.route == this }
