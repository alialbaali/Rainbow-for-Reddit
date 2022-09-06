package com.rainbow.desktop.user

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.itemsIndexed
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.utils.PagingEffect
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.getOrDefault
import com.rainbow.domain.models.User

inline fun LazyGridScope.users(
    state: UIState<List<User>>,
    crossinline onNavigateMainScreen: (MainScreen) -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    crossinline onLoadMore: (User) -> Unit,
) {
    val users = state.getOrDefault(emptyList())

    itemsIndexed(users) { index, user ->
        UserItem(
            user,
            onClick = { onNavigateMainScreen(MainScreen.User(user.name)) },
            onShowSnackbar
        )
        PagingEffect(users, index, onLoadMore)
    }

    if (state.isLoading) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            RainbowProgressIndicator()
        }
    }
}