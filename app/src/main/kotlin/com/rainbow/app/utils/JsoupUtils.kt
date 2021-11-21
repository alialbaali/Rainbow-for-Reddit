package com.rainbow.app.utils

import org.jsoup.Jsoup

val String.imageUrl
    get() = Jsoup.connect(this)
        .ignoreContentType(true)
        .get().select("img")
        .firstOrNull()
        ?.absUrl("src")