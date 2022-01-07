package com.rainbow.common.user

import com.rainbow.common.model.UnSortedListModel
import com.rainbow.domain.models.User

class UserListModel(getUsers: suspend (String?) -> Result<List<User>>) : UnSortedListModel<User>(getUsers) {
    override val User.itemId: String
        get() = this.id
}