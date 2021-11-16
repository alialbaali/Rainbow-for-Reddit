package com.rainbow.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Preview(
    @SerialName("u")
    val url: String? = null, // https://preview.redd.it/qssw9v6f5uz71.jpg?width=108&amp;crop=smart&amp;auto=webp&amp;s=4194cde394e2c13284df3a7cd58dfbaec9616333
    @SerialName("x")
    val x: Int? = null, // 108
    @SerialName("y")
    val y: Int? = null // 81
)