package com.rainbow.local

import com.rainbow.domain.models.Flair
import kotlinx.coroutines.flow.Flow

interface LocalSubredditFlairDataSource {

    val flairs: Flow<List<Flair>>

    val currentFlair: Flow<Flair?>

    fun insertFlair(flair: Flair)

    fun setCurrentFlair(flair: Flair?)

    fun clearFlairs()

}