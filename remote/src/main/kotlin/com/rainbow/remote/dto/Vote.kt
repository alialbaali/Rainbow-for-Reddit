package com.rainbow.remote.dto

enum class Vote(val value: Int) {
    Up(1), Down(-1), Neutral(0),
}