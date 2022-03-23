package com.pi.androidbasehiltmvvm.core.common.deeplink

object InternalDeepLink {
    private const val DOMAIN = "myapp://"

    const val MAIN = "${DOMAIN}main"

    fun makeCustomDeepLink(id: String): String {
        return "${DOMAIN}customDeepLink?id=$id"
    }
}
