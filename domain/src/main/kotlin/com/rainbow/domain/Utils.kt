package com.rainbow.domain


fun colorOf(value: String): Long = value.toLong(16)

fun colorOf(red: String, green: String, blue: String, alpha: String = "FF"): Long = colorOf("$alpha$red$green$blue")