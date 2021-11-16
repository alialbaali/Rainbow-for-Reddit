package com.rainbow.data.utils

const val DefaultLimit = 100

fun String.toLongColor() = removePrefix("#").let { "FF$it" }.toLong(radix = 16)