package com.rainbow.common.comment

import com.rainbow.common.model.SortedListModel
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Sorting
import com.rainbow.domain.models.TimeSorting

class CommentListModel<S : Sorting>(
    initialCommentSorting: S,
    getItems: suspend (S, TimeSorting, String?) -> Result<List<Comment>>,
) : SortedListModel<Comment, S>(initialCommentSorting, getItems) {
    override val Comment.itemId: String
        get() = this.id
}