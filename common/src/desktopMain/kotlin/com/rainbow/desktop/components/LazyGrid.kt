package com.rainbow.desktop.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyGrid(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(16.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(16.dp),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    item: @Composable LazyItemScope.() -> Unit = {},
    content: LazyGridScope.() -> Unit,
) {
//    var cellCount by remember { mutableStateOf(3) }
//    LazyColumn(
//        modifier = Modifier
//            .padding(horizontal = 16.dp)
//            .onSizeChanged { size ->
//                cellCount = when {
//                    size.width < 1000 -> 2
//                    size.width in 1000 until 1500 -> 3
//                    else -> 4
//                }
//            },
//        state = state,
//        verticalArrangement = verticalArrangement,
//        horizontalAlignment = horizontalAlignment,
//    ) {
//        item(content = item)
//        val scope = LazyGridImpl()
//        scope.apply(content)
//
//        val rows = if (scope.totalSize == 0) 0 else 1 + (scope.totalSize - 1) / cellCount
//        items(rows) { rowIndex ->
//            Row(horizontalArrangement = horizontalArrangement) {
//                for (columnIndex in 0 until cellCount) {
//                    val itemIndex = rowIndex * cellCount + columnIndex
//                    if (itemIndex < scope.totalSize) {
//                        Box(
//                            modifier = Modifier.weight(1f, fill = true),
//                            propagateMinConstraints = true
//                        ) {
//                            scope.contentFor(itemIndex, this@items).invoke()
//                        }
//                    } else {
//                        Spacer(Modifier.weight(1f, fill = true))
//                    }
//                }
//            }
//        }
//    }
}

//
//@OptIn(ExperimentalFoundationApi::class)
//internal class LazyGridImpl : LazyGridScope {
//    private val intervals = MutableIntervalList<LazyItemScope.(Int) -> (@Composable () -> Unit)>()
//
//    val totalSize get() = intervals.totalSize
//
//    fun contentFor(index: Int, scope: LazyItemScope): @Composable () -> Unit {
//        val interval = intervals.intervalForIndex(index)
//        val localIntervalIndex = index - interval.startIndex
//
//        return interval.content(scope, localIntervalIndex)
//    }
//
//    override fun item(content: @Composable LazyItemScope.() -> Unit) {
//        intervals.add(1) { @Composable { content() } }
//    }
//
//    override fun items(
//        count: Int,
//        itemContent: @Composable LazyItemScope.(index: Int) -> Unit,
//    ) {
//        intervals.add(count) { @Composable { itemContent(it) } }
//    }
//}
//
//
//private class IntervalHolder<T>(
//    val startIndex: Int,
//    val size: Int,
//    val content: T,
//)
//
//private interface IntervalList<T> {
//    val intervals: List<IntervalHolder<T>>
//    val totalSize: Int
//}
//
//private class MutableIntervalList<T> : IntervalList<T> {
//    private val _intervals = mutableListOf<IntervalHolder<T>>()
//    override val intervals: List<IntervalHolder<T>> = _intervals
//    override var totalSize = 0
//        private set
//
//    fun add(size: Int, content: T) {
//        if (size == 0) {
//            return
//        }
//
//        val interval = IntervalHolder(
//            startIndex = totalSize,
//            size = size,
//            content = content
//        )
//        totalSize += size
//        _intervals.add(interval)
//    }
//}
//
//private fun <T> IntervalList<T>.intervalForIndex(index: Int) =
//    intervals[intervalIndexForItemIndex(index)]
//
//private fun <T> IntervalList<T>.intervalIndexForItemIndex(index: Int) =
//    if (index < 0 || index >= totalSize) {
//        throw IndexOutOfBoundsException("Index $index, size $totalSize")
//    } else {
//        findIndexOfHighestValueLesserThan(intervals, index)
//    }
//
///**
// * Finds the index of the [list] which contains the highest value of [IntervalHolder.startIndex]
// * that is less than or equal to the given [value].
// */
//private fun <T> findIndexOfHighestValueLesserThan(list: List<IntervalHolder<T>>, value: Int): Int {
//    var left = 0
//    var right = list.lastIndex
//
//    while (left < right) {
//        val middle = left + (right - left) / 2
//
//        val middleValue = list[middle].startIndex
//        if (middleValue == value) {
//            return middle
//        }
//
//        if (middleValue < value) {
//            left = middle + 1
//
//            // Verify that the left will not be bigger than our value
//            if (value < list[left].startIndex) {
//                return middle
//            }
//        } else {
//            right = middle - 1
//        }
//    }
//
//    return left
//}