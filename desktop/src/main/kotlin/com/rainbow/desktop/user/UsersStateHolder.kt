package com.rainbow.desktop.user

import com.rainbow.desktop.state.ListStateHolder
import com.rainbow.domain.models.User
import kotlinx.coroutines.flow.Flow

abstract class UsersStateHolder(items: Flow<List<User>>) : ListStateHolder<User>(items)