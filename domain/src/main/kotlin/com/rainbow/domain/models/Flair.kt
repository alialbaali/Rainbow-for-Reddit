package com.rainbow.domain.models

sealed interface Flair {
    class TextFlair(val text: String, val backgroundColor: Long, val textColor: TextColor) : Flair {
        enum class TextColor { Light, Dark, }
    }

    class ImageFlair(val urls: List<String>, val backgroundColor: Long) : Flair
}