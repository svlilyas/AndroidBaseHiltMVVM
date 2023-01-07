package com.pi.data.utils

import com.google.gson.Gson
import timber.log.Timber

/**
 * Converting String To Object
 * @param value to object which send via reified
 */
inline fun <reified T> convertToObject(value: String): T? = try {
    with(value) {
        when (T::class) {
            Boolean::class -> toBoolean() as T
            Float::class -> toFloat() as T
            Int::class -> toInt() as T
            Long::class -> toLong() as T
            Double::class -> toDouble() as T
            String::class -> toString() as T
            else -> {
                Gson().fromJson(this, T::class.java)
            }
        }
    }
} catch (e: Exception) {
    Timber.e(e.message.toString())
    null
}

/**
 * Converting Object To String
 * @param value type [T] to [String]
 */
inline fun <reified T> convertToString(value: T): String? = try {
    with(value) {
        when (T::class) {
            Boolean::class, Float::class, Int::class, Long::class, Double::class, String::class -> toStringOrNull()
            else -> {
                Gson().toJson(value, T::class.java)
            }
        }
    }
} catch (e: Exception) {
    Timber.e(e)
    null
}