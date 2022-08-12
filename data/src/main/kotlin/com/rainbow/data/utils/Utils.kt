package com.rainbow.data.utils

const val DefaultLimit = 5

fun String.toLongColor() = removePrefix("#").let {
    if (this == "transparent")
        "00FFFFFF"
    else
        "FF$it"
}.toLong(radix = 16)

val <T : Enum<T>> Enum<T>.lowercaseName
    get() = name.lowercase()