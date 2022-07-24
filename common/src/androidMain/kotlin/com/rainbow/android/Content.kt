package com.rainbow.android

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import com.rainbow.android.components.*
import com.rainbow.common.components.*
import com.rainbow.android.home.HomeScreen
import com.rainbow.common.message.MessageScreenModel
import com.rainbow.common.model.ListModel
import com.rainbow.android.navigation.Screen
import com.rainbow.common.post.PostScreenModel
import com.rainbow.android.profile.ProfileScreen
import com.rainbow.android.settings.SettingsScreen
import com.rainbow.common.utils.UIState
import com.rainbow.domain.models.*

@OptIn(ExperimentalMaterialNavigationApi::class)
internal fun NavGraphBuilder.content(
    postScreenModelType: UIState<PostScreenModel.Type>,
    messageScreenModel: UIState<MessageScreenModel>,
    sorting: Sorting?,
    timeSorting: TimeSorting?,
    onSortingUpdate: (Sorting) -> Unit,
    onTimeSortingUpdate: (TimeSorting) -> Unit,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onPostClick: (Post) -> Unit,
    onCommentClick: (Comment) -> Unit,
    onMessageClick: (Message) -> Unit,
    onSubredditUpdate: (Subreddit) -> Unit,
    onPostUpdate: (Post) -> Unit,
    onCommentUpdate: (Comment) -> Unit,
    onNavigate: (Screen.Sheet) -> Unit,
    onShowAwards: (Post) -> Unit,
    onShowSnackbar: (String) -> Unit,
    setListModel: (ListModel<*>) -> Unit,
    modifier: Modifier = Modifier,
) {
    composable(Screen.NavigationItem.Home.route) {
        HomeScreen(
            onUserNameClick,
            onSubredditNameClick,
            onPostClick,
            onPostUpdate,
            onPostMenuClick = { onNavigate(Screen.Sheet.PostMenu) },
            onCommentClick,
            onCommentUpdate,
            onCommentMenuClick = { onNavigate(Screen.Sheet.CommentMenu) },
            onShowAwards,
            setListModel,
            modifier,
        )
    }

//    composable(Screen.NavigationItem.Subreddits.route) {
//        CurrentUserSubredditsScreen(
//            onSubredditNameClick,
//            onSubredditUpdate,
//            onShowSnackbar,
//            setListModel,
//            modifier,
//        )
//    }

//    composable(Screen.NavigationItem.Messages.route) {
//        com.rainbow.common.message.MessagesScreen(
//            onMessageClick,
//            onUserNameClick,
//            onSubredditNameClick,
//            onShowSnackbar,
//            setListModel,
//            modifier,
//        )
//    }

    composable(Screen.NavigationItem.Profile.route) {
        ProfileScreen(
            onUserNameClick,
            onSubredditNameClick,
            onPostClick,
            onCommentClick,
            onPostUpdate,
            onCommentUpdate,
            onPostMenuClick = { onNavigate(Screen.Sheet.PostMenu) },
            onShowAwards,
            onShowSnackbar,
            setListModel,
            modifier,
        )
    }

//    composable("${Screen.User.route}/{userName}") {
//        val userName = it.arguments?.getString("userName")!!
//        com.rainbow.common.user.UserScreen(
//            userName,
//            onUserNameClick,
//            onSubredditNameClick,
//            onPostClick,
//            onCommentClick,
//            onPostUpdate,
//            onCommentUpdate,
//            onShowSnackbar,
//            setListModel,
//            modifier,
//        )
//    }
//
//    composable("${Screen.Subreddit.route}/{subredditName}") {
//        val subredditName = it.arguments?.getString("subredditName")!!
//        SubredditScreen(
//            subredditName,
//            onUserNameClick,
//            onSubredditNameClick,
//            onPostClick,
//            onSubredditUpdate,
//            onPostUpdate,
//            onPostMenuClick = { onNavigate(Screen.Sheet.PostMenu) },
//            onShowSnackbar,
//            setListModel,
//            modifier,
//        )
//    }

//    composable(Screen.Post.route) {
//        postScreenModelType.composed(onShowSnackbar) { type ->
//            com.rainbow.common.post.PostScreen(
//                type,
//                onUserNameClick,
//                onSubredditNameClick,
//                onPostUpdate,
//                onCommentUpdate,
//                stickyHeaderContent = null,
//                onShowSnackbar,
//                modifier
//            )
//        }
//    }
//
//    composable(Screen.Message.route) {
//        messageScreenModel.composed(onShowSnackbar) { model ->
//            com.rainbow.common.message.MessageScreen(
//                model,
//                onUserNameClick,
//                onSubredditNameClick,
//                modifier
//            )
//        }
//    }

    composable(Screen.Settings.route) {
        SettingsScreen(onNavigate, modifier)
    }

    bottomSheet(Screen.Sheet.HomePostSorting.route) {
        HomePostSortingBottomSheet(onSortingUpdate)
    }

    bottomSheet(Screen.Sheet.UserPostSorting.route) {
        UserPostSortingBottomSheet(onSortingUpdate)
    }

    bottomSheet(Screen.Sheet.SubredditPostSorting.route) {
        SubredditPostSortingBottomSheet(onSortingUpdate)
    }

    bottomSheet(Screen.Sheet.SearchPostSorting.route) {
        SearchPostSortingBottomSheet(onSortingUpdate)
    }

    bottomSheet(Screen.Sheet.PostCommentSorting.route) {
        PostCommentSortingBottomSheet(onSortingUpdate)
    }

    bottomSheet(Screen.Sheet.Theme.route) {
        ThemeBottomSheet()
    }

    bottomSheet(Screen.Sheet.PostMenu.route) {
//        PostMenuBottomSheet()
    }

    bottomSheet("${Screen.Sheet.Awards.route}/{postId}") {
        val postId = it.arguments?.getString("postId") ?: error("Awards can't be found")
        AwardsBottomSheet(postId)
    }
}