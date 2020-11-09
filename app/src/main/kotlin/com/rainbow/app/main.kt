package com.rainbow.app

import com.rainbow.remote.RedditUserDataSourceImpl
import com.rainbow.remote.client

suspend fun main() {

    val dataSource = RedditUserDataSourceImpl(client)

    dataSource.findUser("LoneWalker20")

}