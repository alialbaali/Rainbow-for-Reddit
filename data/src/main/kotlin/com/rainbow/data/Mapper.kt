package com.rainbow.data

internal fun interface Mapper<in I, out O> {
    fun map(input: I): O
}

internal fun <I, O> List<I>.quickMap(mapper: Mapper<I, O>): List<O> = map { mapper.map(it) }
