package com.rainbow.common.utils

expect class TreeMap<K, V>(m: Map<out K?, V?>?) {
    fun floorEntry(key: K): Map.Entry<K, V>
}