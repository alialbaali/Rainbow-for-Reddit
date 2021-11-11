package com.rainbow.app.utils

import org.jsoup.Jsoup

val String.imageUrl
    get() = Jsoup.connect(this).get().select("img")
        .firstOrNull()
        ?.absUrl("src")