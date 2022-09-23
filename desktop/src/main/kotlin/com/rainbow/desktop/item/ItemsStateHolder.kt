package com.rainbow.desktop.item

import com.rainbow.desktop.state.SortedListStateHolder
import com.rainbow.domain.models.Item
import com.rainbow.domain.models.ItemSorting
import kotlinx.coroutines.flow.Flow

abstract class ItemsStateHolder<S : ItemSorting>(
    initialItemSorting: S,
    items: Flow<List<Item>>
) : SortedListStateHolder<Item, S>(initialItemSorting, items)