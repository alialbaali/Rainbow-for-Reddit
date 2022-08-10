package com.rainbow.desktop.utils

import com.rainbow.desktop.profile.ProfileScreenStateHolder
import com.rainbow.domain.models.Award
import com.rainbow.domain.models.Subreddit
import java.util.*

private val models = listOf(
//    HomeScreenStateHolder.postListModel,
    ProfileScreenStateHolder.hiddenPostListModel,
    ProfileScreenStateHolder.upvotedPostListModel,
    ProfileScreenStateHolder.downvotedPostListModel,
    ProfileScreenStateHolder.hiddenPostListModel,
    ProfileScreenStateHolder.submittedPostListModel,
)
private const val MinimumNumberToFormat = 10_000

private val suffixes = TreeMap(mapOf(1_000 to "k", 1_000_000 to "m"))

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

fun findAwardsByPostId(postId: String): List<Award>? = models.firstNotNullOfOrNull {
    it.items.value.getOrDefault(emptyList()).find { post -> post.id == postId }?.awards
}