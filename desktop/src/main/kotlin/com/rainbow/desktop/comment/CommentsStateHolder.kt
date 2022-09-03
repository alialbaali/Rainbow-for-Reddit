package com.rainbow.desktop.comment

import com.rainbow.desktop.state.SortedListStateHolder
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.UserPostSorting
import kotlinx.coroutines.flow.Flow

abstract class CommentsStateHolder(dataItems: Flow<List<Comment>>) : SortedListStateHolder<Comment, UserPostSorting>(
    UserPostSorting.Default,
    dataItems,
)