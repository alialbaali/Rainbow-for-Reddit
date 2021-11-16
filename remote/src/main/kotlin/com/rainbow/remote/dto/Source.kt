package com.rainbow.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Source(
    @SerialName("u")
    val url: String? = null, // https://preview.redd.it/qssw9v6f5uz71.jpg?width=4032&amp;format=pjpg&amp;auto=webp&amp;s=112781ab21a436b787afbb42430b2803013394d2
    @SerialName("x")
    val x: Int? = null, // 4032
    @SerialName("y")
    val y: Int? = null // 3024
)