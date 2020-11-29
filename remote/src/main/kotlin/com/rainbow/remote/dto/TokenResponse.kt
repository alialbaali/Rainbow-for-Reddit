package com.rainbow.remote.dto

data class TokenResponse(
    val accessToken: String?,
    val refreshToken: String?,
    val tokenType: String?,
    val expiresIn: Long?,
    val scope: String?,
)