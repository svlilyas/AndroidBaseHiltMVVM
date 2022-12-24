package com.pi.data.utils

fun Any?.toStringOrNull(): String? {
    return this?.toString()
}

val String.Companion.empty: String
    get() = ""