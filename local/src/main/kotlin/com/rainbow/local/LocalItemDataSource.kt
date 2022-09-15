package com.rainbow.local

import com.rainbow.domain.models.Item
import kotlinx.coroutines.flow.Flow

interface LocalItemDataSource {

    val profileOverviewItems: Flow<List<Item>>

    val profileSavedItems: Flow<List<Item>>

    val userOverviewItems: Flow<List<Item>>

    fun insertProfileOverviewItem(item: Item)

    fun insertProfileSavedItem(item: Item)

    fun insertUserOverviewItem(item: Item)

    fun <T: Item> updateItem(itemId: String, block: (T) -> T)

    fun clearProfileOverviewItems()

    fun clearProfileSavedItems()

    fun clearUserOverviewItems()

}