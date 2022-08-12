package com.rainbow.desktop.settings

import com.rainbow.desktop.state.StateHolder
import com.rainbow.data.Repos
import com.rainbow.domain.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.*

object SettingsStateHolder : StateHolder() {

    val isSidebarExpanded = Repos.Settings.isSidebarExpanded.stateIn(scope, SharingStarted.Eagerly, true)

    val isUserLoggedIn = Repos.User.isUserLoggedIn.stateIn(scope, SharingStarted.Eagerly, false)

    val isPostFullHeight = Repos.Settings.isFullHeight.stateIn(scope, SharingStarted.Eagerly, false)

    val theme = Repos.Settings.theme.stateIn(scope, SharingStarted.Eagerly, Theme.System)

    val postLayout = Repos.Settings.postLayout.stateIn(scope, SharingStarted.Eagerly, PostLayout.Card)

    val isCommentsCollapsed = Repos.Settings.isCommentsCollapsed.stateIn(scope, SharingStarted.Eagerly, false)

    val isTextSelectionEnabled = Repos.Settings.isTextSelectionEnabled.stateIn(scope, SharingStarted.Eagerly, false)

    val markPostAsRead = Repos.Settings.markPostAsRead.stateIn(scope, SharingStarted.Eagerly, MarkPostAsRead.Default)

    private val mutableSelectedTab = MutableStateFlow(SettingsTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    val homePostSorting = Repos.Settings.homePostSorting
        .stateIn(scope, SharingStarted.Eagerly, HomePostSorting.Default)

    val profilePostSorting = Repos.Settings.profilePostSorting
        .stateIn(scope, SharingStarted.Eagerly, ProfilePostSorting.Default)

    val subredditPostSorting = Repos.Settings.subredditPostSorting
        .stateIn(scope, SharingStarted.Eagerly, SubredditPostSorting.Default)

    val userPostSorting = Repos.Settings.userPostSorting
        .stateIn(scope, SharingStarted.Eagerly, UserPostSorting.Default)

    val searchPostSorting = Repos.Settings.searchPostSorting
        .stateIn(scope, SharingStarted.Eagerly, SearchPostSorting.Default)

    val postCommentSorting = Repos.Settings.postCommentSorting
        .stateIn(scope, SharingStarted.Eagerly, PostCommentSorting.Default)

    fun setIsSidebarExpanded(value: Boolean) = scope.launch {
        Repos.Settings.setIsSidebarExpanded(value)
    }

    fun loginUser(uuid: UUID, onSuccess: () -> Unit, onFailure: (Throwable) -> Unit) = scope.launch {
        Repos.User.loginUser(uuid)
            .onSuccess {
                onSuccess()
                Repos.User.getCurrentUser()
            }
            .onFailure(onFailure)
    }

    fun logoutUser() = scope.launch {
        Repos.User.logoutUser()
    }

    fun setIsPostFullHeight(value: Boolean) = scope.launch {
        Repos.Settings.setIsPostFullHeight(value)
    }

    fun setPostLayout(value: PostLayout) = scope.launch {
        Repos.Settings.setPostLayout(value)
    }

    fun setTheme(value: Theme) = scope.launch {
        Repos.Settings.setTheme(value)
    }

    fun setHomePostSorting(value: HomePostSorting) = scope.launch {
        Repos.Settings.setHomePostSorting(value)
    }

    fun setProfilePostSorting(value: ProfilePostSorting) = scope.launch {
        Repos.Settings.setProfilePostSorting(value)
    }

    fun setSubredditPostSorting(value: SubredditPostSorting) = scope.launch {
        Repos.Settings.setSubredditPostSorting(value)
    }

    fun setUserPostSorting(value: UserPostSorting) = scope.launch {
        Repos.Settings.setUserPostSorting(value)
    }

    fun setSearchPostSorting(value: SearchPostSorting) = scope.launch {
        Repos.Settings.setSearchPostSorting(value)
    }

    fun setPostCommentSorting(value: PostCommentSorting) = scope.launch {
        Repos.Settings.setPostCommentSorting(value)
    }

    fun setIsCommentsCollapsed(value: Boolean) = scope.launch {
        Repos.Settings.setIsCommentsCollapsed(value)
    }

    fun setIsTextSelectionEnabled(value: Boolean) = scope.launch {
        Repos.Settings.setIsTextSelectionEnabled(value)
    }

    fun setMarkPostAsRead(value: MarkPostAsRead) = scope.launch {
        Repos.Settings.setMarkPostAsRead(value)
    }

    fun selectTab(tab: SettingsTab) {
        mutableSelectedTab.value = tab
    }
}