package com.rainbow.app.comment

import com.rainbow.app.model.UnSortedListModel
import com.rainbow.domain.models.Comment

class HomeCommentListModel(
    getComments: suspend (String?) -> Result<List<Comment>>,
) : UnSortedListModel<Comment>(getComments) {
    override val Comment.itemId: String
        get() = this.id
}