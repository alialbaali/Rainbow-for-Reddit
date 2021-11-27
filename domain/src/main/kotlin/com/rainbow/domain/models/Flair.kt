package com.rainbow.domain.models

data class Flair(
    val id: String,
    val types: List<Type>,
    val backgroundColor: Long,
    val textColor: TextColor,
) {
    sealed interface Type {
        data class Text(val text: String) : Type
        data class Image(val url: String) : Type
    }

    enum class TextColor { Light, Dark, }
}