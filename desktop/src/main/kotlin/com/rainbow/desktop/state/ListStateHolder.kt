package com.rainbow.desktop.state

import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.getOrDefault
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class ListStateHolder<T : Any>(private val dataItems: Flow<List<T>>) : StateHolder() {

    protected val mutableItems = MutableStateFlow<UIState<List<T>>>(UIState.Empty)
    val items get() = mutableItems.asStateFlow()
    private val currentItems get() = items.value.getOrDefault(emptyList())

    protected val mutableLastItem = MutableStateFlow<T?>(null)
    private val lastItem get() = mutableLastItem.asStateFlow()

    private var job: Job? = null

    protected abstract suspend fun getItems(lastItem: T?): Result<Unit>

    fun loadItems() {
        job?.cancel()
        job = scope.launch {
            loadNewItems()
            loadLastItem()
        }
    }

    open fun CoroutineScope.loadNewItems() {
        launch {
            mutableLastItem.value = null
            mutableItems.value = UIState.Loading()
            getItems(lastItem = null)
                .onSuccess {
                    launch {
                        dataItems
                            .map { UIState.Success(it) }
                            .collect { mutableItems.value = it }
                    }
                }
                .onFailure { mutableItems.value = UIState.Failure(null, it) }
        }
    }

    private fun CoroutineScope.loadLastItem() {
        launch {
            lastItem
                .filterNotNull()
                .collect { lastItem ->
                    mutableItems.value = UIState.Loading(currentItems)
                    launch {
                        getItems(lastItem)
                            .onFailure { mutableItems.value = UIState.Failure(currentItems, it) }
                    }
                }
        }
    }

    fun setLastItem(lastItem: T?) {
        mutableLastItem.value = lastItem
    }

}