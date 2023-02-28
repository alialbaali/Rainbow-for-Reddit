package com.rainbow.remote

import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.inputStream

object RainbowProperties {

    private val properties = Properties().apply {
        load(Path("rainbow.properties").inputStream())
    }

    val RainbowUrl = properties.getProperty("rainbow_url")!!
    val RainbowHost = properties.getProperty("rainbow_host")!!
    val ClientId = properties.getProperty("client_id")!!
    val ClientPassword = properties.getProperty("client_password")!!

}