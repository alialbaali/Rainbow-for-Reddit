package com.rainbow.data.utils

const val DefaultLimit = 25

internal fun String.toLongColor(): Long = removePrefix("#").let { value ->
    if (this == "transparent")
        "00FFFFFF"
    else
        "FF$value"
}.toLong(radix = 16)

internal val <T : Enum<T>> Enum<T>.lowercaseName
    get() = name.lowercase()