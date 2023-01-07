package com.pi.data.utils

import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.file
import java.io.File

fun Any?.toStringOrNull(): String? {
    return this?.toString()
}

val String.Companion.empty: String
    get() = ""

/** Exposes the underlying file. */
val EncryptedFile.file: File
    get() = file