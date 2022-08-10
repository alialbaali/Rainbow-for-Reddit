package com.rainbow.desktop.comment

import com.rainbow.desktop.model.UnSortedListStateHolder
import com.rainbow.domain.models.Comment

class HomeCommentListStateHolder(
    getComments: suspend (String?) -> Result<List<Comment>>,
) : UnSortedListStateHolder<Comment>(getComments) {
    override val Comment.itemId: String
        get() = this.id
}