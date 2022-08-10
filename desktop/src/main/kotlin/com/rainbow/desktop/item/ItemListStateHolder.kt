package com.rainbow.desktop.item

import com.rainbow.desktop.model.SortedListStateHolder
import com.rainbow.data.Repos
import com.rainbow.domain.models.Item
import com.rainbow.domain.models.PostLayout
import com.rainbow.domain.models.Sorting
import com.rainbow.domain.models.TimeSorting
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ItemListStateHolder<S : Sorting>(
    initialItemSorting: S,
    getItems: suspend (S, TimeSorting, String?) -> Result<List<Item>>,
) : SortedListStateHolder<Item, S>(initialItemSorting, getItems) {
    override val Item.itemId: String
        get() = this.id

    val postLayout = Repos.Settings.postLayout.stateIn(scope, SharingStarted.Lazily, PostLayout.Card)
}