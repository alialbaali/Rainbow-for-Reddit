package com.rainbow.desktop.user

import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.itemsIndexed
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.utils.PagingEffect
import com.rainbow.domain.models.User

fun LazyGridScope.users(
    users: List<User>,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    onLoadMore: (User) -> Unit,
) {
    itemsIndexed(users) { index, user ->
        UserItem(
            user,
            onClick = { onNavigateMainScreen(MainScreen.User(user.name)) },
            onShowSnackbar
        )
        PagingEffect(index, users.lastIndex) {
            onLoadMore(user)
        }
    }
}