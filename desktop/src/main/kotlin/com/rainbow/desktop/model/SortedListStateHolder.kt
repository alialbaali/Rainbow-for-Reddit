package com.rainbow.desktop.model

import com.rainbow.desktop.utils.UIState
import com.rainbow.domain.models.Sorting
import com.rainbow.domain.models.TimeSorting
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*

abstract class SortedListStateHolder<T : Any, S : Sorting>(initialSorting: S, items: Flow<List<T>>) : ListStateHolder<T>() {

    private val mutableSorting = MutableStateFlow(initialSorting)
    val sorting get() = mutableSorting.asStateFlow()

    private val mutableTimeSorting = MutableStateFlow(TimeSorting.Default)
    val timeSorting get() = mutableTimeSorting.asStateFlow()

    private var previousJob: Job? = null

    init {
        combine(
            sorting,
            timeSorting,
        ) { sorting, timeSorting ->
            previousJob?.cancel()
            mutableLastItem.value = null
            mutableItems.value = UIState.Loading()
            loadNewItems(sorting, timeSorting)
                .onSuccess {
                    previousJob = items
                        .map { UIState.Success(it) }
                        .onEach { mutableItems.value = it }
                        .launchIn(scope)
                }
                .onFailure { mutableItems.value = UIState.Failure(null, it) }
        }.launchIn(scope)
    }

    abstract suspend fun loadNewItems(sorting: S, timeSorting: TimeSorting): Result<Unit>

    fun setSorting(sorting: S) {
        mutableSorting.value = sorting
    }

    fun setTimeSorting(timeSorting: TimeSorting) {
        mutableTimeSorting.value = timeSorting
    }

}