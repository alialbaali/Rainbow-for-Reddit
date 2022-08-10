package com.rainbow.desktop.comment

import com.rainbow.desktop.model.SortedListStateHolder
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Sorting
import com.rainbow.domain.models.TimeSorting

class CommentListStateHolder<S : Sorting>(
    initialCommentSorting: S,
    getItems: suspend (S, TimeSorting, String?) -> Result<List<Comment>>,
) : SortedListStateHolder<Comment, S>(initialCommentSorting, getItems) {
    override val Comment.itemId: String
        get() = this.id
}