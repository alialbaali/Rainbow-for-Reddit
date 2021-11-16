package com.rainbow.domain.models

sealed interface Flair {
    data class TextFlair(val text: String) : Flair
    data class ImageFlair(val url: String) : Flair
    enum class TextColor { Light, Dark, }
}