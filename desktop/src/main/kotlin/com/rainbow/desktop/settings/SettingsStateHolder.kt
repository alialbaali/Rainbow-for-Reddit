package com.rainbow.desktop.settings

import com.rainbow.data.Repos
import com.rainbow.desktop.state.StateHolder
import com.rainbow.domain.models.*
import com.rainbow.domain.repository.SettingsRepository
import com.rainbow.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.*

class SettingsStateHolder(
    private val settingsRepository: SettingsRepository = Repos.Settings,
    private val userRepository: UserRepository = Repos.User,
) : StateHolder() {

    val isUserLoggedIn = userRepository.isUserLoggedIn.stateIn(scope, SharingStarted.Eagerly, false)

    val theme = settingsRepository.theme.stateIn(scope, SharingStarted.Eagerly, Theme.System)

    val postLayout = settingsRepository.postLayout.stateIn(scope, SharingStarted.Eagerly, PostLayout.Default)

    val isCommentsCollapsed = settingsRepository.isCommentsCollapsed.stateIn(scope, SharingStarted.Eagerly, false)

    val isTextSelectionEnabled = settingsRepository.isTextSelectionEnabled.stateIn(scope, SharingStarted.Eagerly, false)

    val markPostAsRead =
        settingsRepository.markPostAsRead.stateIn(scope, SharingStarted.Eagerly, MarkPostAsRead.Default)

    private val mutableSelectedTab = MutableStateFlow(SettingsTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    val homePostSorting = settingsRepository.homePostSorting
        .stateIn(scope, SharingStarted.Eagerly, HomePostSorting.Default)

    val profilePostSorting = settingsRepository.profilePostSorting
        .stateIn(scope, SharingStarted.Eagerly, ProfilePostSorting.Default)

    val subredditPostSorting = settingsRepository.subredditPostSorting
        .stateIn(scope, SharingStarted.Eagerly, SubredditPostSorting.Default)

    val userPostSorting = settingsRepository.userPostSorting
        .stateIn(scope, SharingStarted.Eagerly, UserPostSorting.Default)

    val searchPostSorting = settingsRepository.searchPostSorting
        .stateIn(scope, SharingStarted.Eagerly, SearchPostSorting.Default)

    val postCommentSorting = settingsRepository.postCommentSorting
        .stateIn(scope, SharingStarted.Eagerly, PostCommentSorting.Default)

    companion object {
        val Instance = SettingsStateHolder()
    }

    fun loginUser(uuid: UUID, onSuccess: () -> Unit, onFailure: (Throwable) -> Unit) = scope.launch {
        userRepository.loginUser(uuid)
            .onSuccess {
                onSuccess()
                userRepository.getCurrentUser()
            }
            .onFailure(onFailure)
    }

    fun logoutUser() = scope.launch {
        userRepository.logoutUser()
    }

    fun setPostLayout(value: PostLayout) = scope.launch {
        settingsRepository.setPostLayout(value)
    }

    fun setTheme(value: Theme) = scope.launch {
        settingsRepository.setTheme(value)
    }

    fun setHomePostSorting(value: HomePostSorting) = scope.launch {
        settingsRepository.setHomePostSorting(value)
    }

    fun setProfilePostSorting(value: ProfilePostSorting) = scope.launch {
        settingsRepository.setProfilePostSorting(value)
    }

    fun setSubredditPostSorting(value: SubredditPostSorting) = scope.launch {
        settingsRepository.setSubredditPostSorting(value)
    }

    fun setUserPostSorting(value: UserPostSorting) = scope.launch {
        settingsRepository.setUserPostSorting(value)
    }

    fun setSearchPostSorting(value: SearchPostSorting) = scope.launch {
        settingsRepository.setSearchPostSorting(value)
    }

    fun setPostCommentSorting(value: PostCommentSorting) = scope.launch {
        settingsRepository.setPostCommentSorting(value)
    }

    fun setIsCommentsCollapsed(value: Boolean) = scope.launch {
        settingsRepository.setIsCommentsCollapsed(value)
    }

    fun setIsTextSelectionEnabled(value: Boolean) = scope.launch {
        settingsRepository.setIsTextSelectionEnabled(value)
    }

    fun setMarkPostAsRead(value: MarkPostAsRead) = scope.launch {
        settingsRepository.setMarkPostAsRead(value)
    }

    fun selectTab(tab: SettingsTab) {
        mutableSelectedTab.value = tab
    }
}