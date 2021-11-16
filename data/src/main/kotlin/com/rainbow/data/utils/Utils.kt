package com.rainbow.data.utils

const val DefaultLimit = 100

fun String.toLongColor() = removePrefix("#").let {
    if (this == "transparent")
        "00FFFFFF"
    else
        "FF$it"
}.toLong(radix = 16)