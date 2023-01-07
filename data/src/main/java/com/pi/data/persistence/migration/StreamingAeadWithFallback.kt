package com.pi.data.persistence.migration

import com.google.crypto.tink.Aead
import com.google.crypto.tink.StreamingAead
import com.pi.data.persistence.newDecryptedStream
import java.io.IOException
import java.io.InputStream

/** Wrapper around [StreamingAead] with decryption [fallback] to [Aead]. */
internal class StreamingAeadWithFallback(
    private val delegate: StreamingAead,
    private val fallback: Aead,
) : StreamingAead by delegate {

    override fun newDecryptingStream(
        ciphertextSource: InputStream,
        associatedData: ByteArray
    ): InputStream {
        return DecryptingStreamWithFallback(
            stream = delegate.newDecryptingStream(ciphertextSource, associatedData),
            fallbackStream = { fallback.newDecryptedStream(ciphertextSource) },
        )
    }

    class DecryptingStreamWithFallback(
        private var stream: InputStream,
        private val fallbackStream: () -> InputStream,
    ) : InputStream() {

        /** Implementation copied from com.google.crypto.tink.subtle.StreamingAeadDecryptingStream.read */
        override fun read(): Int {
            val oneByte = ByteArray(size = 1)
            return if (read(oneByte) == 1) oneByte[0].toInt() and 0xff else -1
        }

        override fun read(b: ByteArray, offset: Int, length: Int): Int {
            return try {
                stream.read(b, offset, length)
            } catch (e: IOException) {
                if (!e.isProbablyEncryptedWithAeadException()) throw e
                stream = fallbackStream()
                // Try to read again from the new delegate
                stream.read(b, offset, length)
            }
        }

        override fun skip(n: Long): Long = stream.skip(n)
        override fun available(): Int = stream.available()
        override fun close() = stream.close()
        override fun mark(readlimit: Int) = stream.mark(readlimit)
        override fun reset() = stream.reset()
        override fun markSupported(): Boolean = stream.markSupported()
    }
}

/**
 * StreamingAead throws IOException with specific message when trying to decrypt data encrypted with Aead.
 * See [com.google.crypto.tink.streamingaead.InputStreamDecrypter.read]
 */
internal fun IOException.isProbablyEncryptedWithAeadException(): Boolean =
    message == "No matching key found for the ciphertext in the stream."

/**
 * Returns [this] [StreamingAead] with decryption fallback to the given [aead].
 * It may be useful for migration from [Aead] to [StreamingAead].
 */
public fun StreamingAead.withDecryptionFallback(aead: Aead): StreamingAead {
    return StreamingAeadWithFallback(delegate = this, fallback = aead)
}
