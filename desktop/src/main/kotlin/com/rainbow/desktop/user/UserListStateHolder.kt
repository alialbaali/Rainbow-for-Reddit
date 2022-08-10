package com.rainbow.desktop.user

import com.rainbow.desktop.model.UnSortedListStateHolder
import com.rainbow.domain.models.User

class UserListStateHolder(getUsers: suspend (String?) -> Result<List<User>>) : UnSortedListStateHolder<User>(getUsers) {
    override val User.itemId: String
        get() = this.id
}