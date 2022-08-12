package com.rainbow.desktop.home

import com.rainbow.desktop.state.ListStateHolder
import com.rainbow.domain.models.Comment
import kotlinx.coroutines.flow.Flow

abstract class HomeScreenCommentsStateHolder(dataItems: Flow<List<Comment>>) : ListStateHolder<Comment>(dataItems)