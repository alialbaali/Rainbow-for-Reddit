package com.rainbow.desktop.item

import com.rainbow.data.Repos
import com.rainbow.desktop.state.SortedListStateHolder
import com.rainbow.domain.models.Item
import com.rainbow.domain.models.PostLayout
import com.rainbow.domain.models.Sorting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

abstract class ItemsStateHolder<S : Sorting>(
    initialItemSorting: S,
    items: Flow<List<Item>>
) : SortedListStateHolder<Item, S>(initialItemSorting, items) {

    val postLayout = Repos.Settings.postLayout.stateIn(scope, SharingStarted.Lazily, PostLayout.Card)
}