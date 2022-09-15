package com.rainbow.local

import com.rainbow.domain.models.Flair
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalSubredditFlairDataSourceImpl : LocalSubredditFlairDataSource {

    private val mutableFlairs = MutableStateFlow(emptyList<Flair>())
    override val flairs get() = mutableFlairs.asStateFlow()

    private val mutableCurrentFlair = MutableStateFlow<Flair?>(null)
    override val currentFlair get() = mutableCurrentFlair.asStateFlow()

    override fun insertFlair(flair: Flair) {
        mutableFlairs.value += flair
    }

    override fun setCurrentFlair(flair: Flair?) {
        mutableCurrentFlair.value = flair
    }

    override fun clearFlairs() {
        mutableFlairs.value = emptyList()
    }

}