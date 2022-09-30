package com.rainbow.desktop.utils

import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Subreddit
import java.util.*

private const val MinimumNumberToFormat = 10_000

private val suffixes = TreeMap(mapOf(1_000 to "K", 1_000_000 to "M"))

fun Int.format(): String {
//    Int.MIN_VALUE == -Int.MIN_VALUE, so we need an adjustment here
    if (this == Int.MIN_VALUE) return Int.MIN_VALUE.inc().format()
    if (this < 0) return "-" + this.unaryMinus().format()
    if (this < MinimumNumberToFormat) return this.toString() //deal with easy case
    val (divideBy, suffix) = suffixes.floorEntry(this)
    val truncated = this / (divideBy / 10) //the number part of the output times 10
    val hasDecimal = truncated < 1_000 && truncated / 10.0 != (truncated / 10).toDouble()
    return if (hasDecimal) (truncated / 10.0).toString() + suffix else (truncated / 10).toString() + suffix
}

fun List<Subreddit>.filterContent(searchTerm: String) = filter {
    it.name.contains(searchTerm, ignoreCase = true) || it.shortDescription.contains(searchTerm)
}

fun List<Comment>.countRecursively(): Int {
    return count { it.type !is Comment.Type.MoreComments } + sumOf { comment ->
        when (val type = comment.type) {
            is Comment.Type.MoreComments -> type.replies.count()
            else -> comment.replies.countRecursively()
        }
    }
}