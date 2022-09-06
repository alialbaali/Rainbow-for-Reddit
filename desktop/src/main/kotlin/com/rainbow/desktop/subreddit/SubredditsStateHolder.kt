package com.rainbow.desktop.subreddit

import com.rainbow.desktop.state.ListStateHolder
import com.rainbow.domain.models.Subreddit
import kotlinx.coroutines.flow.Flow

abstract class SubredditsStateHolder(items: Flow<List<Subreddit>>) : ListStateHolder<Subreddit>(items)