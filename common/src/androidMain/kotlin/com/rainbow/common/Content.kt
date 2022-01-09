package com.rainbow.common

import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import com.rainbow.common.home.HomeScreen
import com.rainbow.common.message.MessageScreen
import com.rainbow.common.message.MessageScreenModel
import com.rainbow.common.message.MessagesScreen
import com.rainbow.common.model.ListModel
import com.rainbow.common.navigation.Screen
import com.rainbow.common.post.PostScreen
import com.rainbow.common.post.PostScreenModel
import com.rainbow.common.profile.ProfileScreen
import com.rainbow.common.settings.SettingsScreen
import com.rainbow.common.subreddit.CurrentUserSubredditsScreen
import com.rainbow.common.subreddit.SubredditScreen
import com.rainbow.common.user.UserScreen
import com.rainbow.common.utils.UIState
import com.rainbow.common.utils.composed
import com.rainbow.domain.models.*

@OptIn(ExperimentalMaterialNavigationApi::class)
internal fun NavGraphBuilder.content(
    focusRequester: FocusRequester,
    postScreenModelType: UIState<PostScreenModel.Type>,
    messageScreenModel: UIState<MessageScreenModel>,
    sorting: Sorting?,
    timeSorting: TimeSorting?,
    onPostSortingUpdate: (Sorting) -> Unit,
    onTimeSortingUpdate: (TimeSorting) -> Unit,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onPostClick: (Post) -> Unit,
    onCommentClick: (Comment) -> Unit,
    onMessageClick: (Message) -> Unit,
    onSubredditUpdate: (Subreddit) -> Unit,
    onPostUpdate: (Post) -> Unit,
    onCommentUpdate: (Comment) -> Unit,
    onShowSnackbar: (String) -> Unit,
    setListModel: (ListModel<*>) -> Unit,
    modifier: Modifier = Modifier,
) {
    composable(Screen.NavigationItem.Home.route) {
        HomeScreen(
            focusRequester,
            onUserNameClick,
            onSubredditNameClick,
            onPostClick,
            onCommentClick,
            onPostUpdate,
            onCommentUpdate,
            onShowSnackbar,
            setListModel,
        )
    }

    composable(Screen.NavigationItem.Subreddits.route) {
        CurrentUserSubredditsScreen(
            onSubredditNameClick,
            onSubredditUpdate,
            onShowSnackbar,
            setListModel
        )
    }

    composable(Screen.NavigationItem.Messages.route) {
        MessagesScreen(
            onMessageClick,
            onUserNameClick,
            onSubredditNameClick,
            onShowSnackbar,
            setListModel,
            modifier,
        )
    }

    composable(Screen.NavigationItem.Profile.route) {
        ProfileScreen(
            focusRequester,
            onUserNameClick,
            onSubredditNameClick,
            onPostClick,
            onCommentClick,
            onPostUpdate,
            onCommentUpdate,
            onShowSnackbar,
            setListModel,
            modifier,
        )
    }

    composable("${Screen.User.route}/{userName}") {
        val userName = it.arguments?.getString("userName")!!
        UserScreen(
            userName,
            focusRequester,
            onUserNameClick,
            onSubredditNameClick,
            onPostClick,
            onCommentClick,
            onPostUpdate,
            onCommentUpdate,
            onShowSnackbar,
            setListModel,
            modifier,
        )
    }

    composable("${Screen.Subreddit.route}/{subredditName}") {
        val subredditName = it.arguments?.getString("subredditName")!!
        SubredditScreen(
            subredditName,
            focusRequester,
            onUserNameClick,
            onSubredditNameClick,
            onPostClick,
            onSubredditUpdate,
            onPostUpdate,
            onShowSnackbar,
            setListModel,
            modifier,
        )
    }

    composable(Screen.Post.route) {
        postScreenModelType.composed(onShowSnackbar) { type ->
            PostScreen(
                type,
                focusRequester,
                onUserNameClick,
                onSubredditNameClick,
                onPostUpdate,
                onCommentUpdate,
                onShowSnackbar,
                modifier
            )
        }
    }

    composable(Screen.Message.route) {
        messageScreenModel.composed(onShowSnackbar) { model ->
            MessageScreen(
                model,
                onUserNameClick,
                onSubredditNameClick,
                modifier
            )
        }
    }

    composable(Screen.Settings.route) {
        SettingsScreen()
    }

    bottomSheet(Screen.Sheet.HomePostSorting.route) {
        SortingBottomSheet<HomePostSorting>(onPostSortingUpdate)
    }

    bottomSheet(Screen.Sheet.UserPostSorting.route) {
        SortingBottomSheet<UserPostSorting>(onPostSortingUpdate)
    }

    bottomSheet(Screen.Sheet.SubredditPostSorting.route) {
        SortingBottomSheet<SubredditPostSorting>(onPostSortingUpdate)
    }

    bottomSheet(Screen.Sheet.SearchPostSorting.route) {
        SortingBottomSheet<SearchPostSorting>(onPostSortingUpdate)
    }

    bottomSheet(Screen.Sheet.PostCommentSorting.route) {
        SortingBottomSheet<PostCommentSorting>(onPostSortingUpdate)
    }
}