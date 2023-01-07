package com.pi.data.persistence.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.crypto.tink.StreamingAead
import com.pi.data.persistence.migration.isProbablyEncryptedWithAeadException
import java.io.*
import java.security.GeneralSecurityException

/** Interface for [Serializer] supporting encryption. */
sealed interface EncryptingSerializer<T> : Serializer<T>

internal abstract class WrappingEncryptingSerializer<T> : EncryptingSerializer<T> {

    protected abstract val delegate: Serializer<T>

    final override val defaultValue: T
        get() = delegate.defaultValue

    final override suspend fun readFrom(input: InputStream): T {
        return try {
            readEncryptedFrom(input)
        } catch (e: GeneralSecurityException) {
            throw CorruptionException("DataStore decryption failed", e)
        }
    }

    /** Reads encrypted data from the given [input]. */
    protected abstract suspend fun readEncryptedFrom(input: InputStream): T
}

internal class StreamingAeadEncryptingSerializer<T>(
    private val streamingAead: StreamingAead,
    private val associatedData: ByteArray,
    override val delegate: Serializer<T>,
) : WrappingEncryptingSerializer<T>() {

    override suspend fun readEncryptedFrom(input: InputStream): T {
        return try {
            streamingAead.newDecryptingStream(input, associatedData).use { decryptingStream ->
                delegate.readFrom(decryptingStream)
            }
        } catch (e: IOException) {
            throw e.toFriendlyException()
        }
    }

    private fun IOException.toFriendlyException(): Exception {
        return if (isProbablyEncryptedWithAeadException()) {
            CorruptionException(
                "Can not decrypt DataStore using StreamingAead.\n" +
                        "Probably you have not closed output stream in the `writeTo` method of the serializer or \n" +
                        "if you had used library before make sure you have added fallback to Aead:\n" +
                        "https://github.com/osipxd/encrypted-datastore#migration",
                cause = this,
            )
        } else {
            this
        }
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        streamingAead.newEncryptingStream(output, associatedData).use { encryptingStream ->
            delegate.writeTo(t, encryptingStream)
        }
    }
}

/**
 * Adds encryption to [this] serializer using the given [StreamingAead] and [associatedData]
 * as an associated authenticated data.
 *
 * Associated data is authenticated but not encrypted. In some cases, binding ciphertext
 * to associated data strengthens security:
 * [I want to bind ciphertext to its context](https://developers.google.com/tink/bind-ciphertext)
 */
fun <T> Serializer<T>.encrypted(
    streamingAead: StreamingAead,
    associatedData: ByteArray = byteArrayOf(),
): EncryptingSerializer<T> =
    StreamingAeadEncryptingSerializer(streamingAead, associatedData, delegate = this)
