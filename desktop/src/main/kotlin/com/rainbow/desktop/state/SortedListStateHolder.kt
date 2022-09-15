package com.rainbow.desktop.state

import com.rainbow.desktop.utils.UIState
import com.rainbow.domain.models.ItemSorting
import com.rainbow.domain.models.TimeSorting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class SortedListStateHolder<T : Any, S : ItemSorting>(initialSorting: S, private val dataItems: Flow<List<T>>) :
    ListStateHolder<T>(dataItems) {

    private val mutableSorting = MutableStateFlow(initialSorting)
    val sorting get() = mutableSorting.asStateFlow()

    private val mutableTimeSorting = MutableStateFlow(TimeSorting.Default)
    val timeSorting get() = mutableTimeSorting.asStateFlow()

    final override suspend fun getItems(lastItem: T?): Result<Unit> {
        return getItems(sorting.value, timeSorting.value, lastItem)
    }

    protected abstract suspend fun getItems(sorting: S, timeSorting: TimeSorting, lastItem: T?): Result<Unit>

    final override fun CoroutineScope.loadNewItems() {
        launch {
            combine(
                sorting,
                timeSorting,
            ) { sorting, timeSorting ->
                mutableLastItem.value = null
                mutableItems.value = UIState.Loading()
                launch {
                    getItems(sorting, timeSorting, lastItem = null)
                        .onSuccess {
                            launch {
                                dataItems
                                    .map { UIState.Success(it) }
                                    .collect { mutableItems.value = it }
                            }
                        }
                        .onFailure { mutableItems.value = UIState.Failure(null, it) }
                }
            }.collect()
        }
    }

    fun setSorting(sorting: S) {
        mutableSorting.value = sorting
    }

    fun setTimeSorting(timeSorting: TimeSorting) {
        mutableTimeSorting.value = timeSorting
    }

}