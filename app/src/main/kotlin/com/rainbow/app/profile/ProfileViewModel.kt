package com.rainbow.app.profile

import com.rainbow.app.profile.ProfileViewModel.UserIntent
import com.rainbow.app.profile.ProfileViewModel.UserState
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.toUIState
import com.rainbow.app.utils.withState
import com.rainbow.data.Repos
import com.rainbow.domain.ViewModel
import com.rainbow.domain.models.User
import com.rainbow.domain.repository.UserRepository

private const val CurrentUserName = "LoneWalker20"

class ProfileViewModel(
    private val userRepository: UserRepository = Repos.User,
) : ViewModel<UserIntent, UserState>(UserState()) {

    init {
        emitIntent(UserIntent.GetCurrentUser)
    }

    override suspend fun onIntent(intent: UserIntent): UserState = when (intent) {
        UserIntent.GetCurrentUser -> withState {
            copy(
                currentUser = userRepository
                    .getCurrentUser()
                    .toUIState()
            )
        }
    }

    sealed class UserIntent : Intent {
        object GetCurrentUser : UserIntent()
    }

    data class UserState(
        val currentUser: UIState<User> = UIState.Loading,
    ) : State

}