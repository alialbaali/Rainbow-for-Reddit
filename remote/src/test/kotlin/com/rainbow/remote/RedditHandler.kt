package com.rainbow.remote

import com.rainbow.remote.routes.subredditRoute
import com.rainbow.remote.routes.userRoute
import com.rainbow.remote.routes.utils.fullHost
import com.rainbow.remote.routes.utils.respondNotImplemented
import com.rainbow.remote.routes.utils.urlPath
import io.ktor.client.engine.mock.*

internal fun MockEngineConfig.redditHandler() {
    addHandler { request ->

        val url = request.url.fullHost

//        require(url == OauthUrl) {
//            "Wrong Url. Make sure the url is $OauthUrl and not $url"
//        }

        userRoute(request) ?: subredditRoute(request) ?: respondNotImplemented().also { println(request.urlPath) }
    }

}