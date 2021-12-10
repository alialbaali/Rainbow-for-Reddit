package com.rainbow.app.user

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rainbow.app.components.LazyGrid
import com.rainbow.domain.models.User

enum class UserType { Default, Search }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Users(
    users: List<User>,
    userType: UserType,
    onClick: (User) -> Unit,
    onLoadMore: (User) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyGrid {
        items(users) { user ->
            when (userType) {
                UserType.Default -> UserItem(user, onClick, onShowSnackbar)
                UserType.Search -> UserItem(user, onClick, onShowSnackbar)
            }
        }
    }
}
