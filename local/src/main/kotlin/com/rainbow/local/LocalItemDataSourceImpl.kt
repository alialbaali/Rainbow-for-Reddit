package com.rainbow.local

import com.rainbow.domain.models.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalItemDataSourceImpl : LocalItemDataSource {

    private val mutableProfileOverviewItems = MutableStateFlow(emptyList<Item>())
    override val profileOverviewItems get() = mutableProfileOverviewItems.asStateFlow()

    private val mutableProfileSavedItems = MutableStateFlow(emptyList<Item>())
    override val profileSavedItems get() = mutableProfileSavedItems.asStateFlow()

    private val mutableUserOverviewItems = MutableStateFlow(emptyList<Item>())
    override val userOverviewItems get() = mutableUserOverviewItems.asStateFlow()

    override fun insertProfileOverviewItem(item: Item) {
        mutableProfileOverviewItems.value = profileOverviewItems.value + item
    }

    override fun insertProfileSavedItem(item: Item) {
        mutableProfileSavedItems.value = profileSavedItems.value + item
    }

    override fun insertUserOverviewItem(item: Item) {
        mutableUserOverviewItems.value = userOverviewItems.value + item
    }

    override fun clearProfileOverviewItems() {
        mutableProfileOverviewItems.value = emptyList()
    }

    override fun clearProfileSavedItems() {
        mutableProfileSavedItems.value = emptyList()
    }

    override fun clearUserOverviewItems() {
        mutableUserOverviewItems.value = emptyList()
    }

}