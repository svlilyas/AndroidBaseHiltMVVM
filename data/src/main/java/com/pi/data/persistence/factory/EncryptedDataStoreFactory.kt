package com.pi.data.persistence.factory

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.file
import androidx.security.crypto.streamingAead
import com.google.crypto.tink.Aead
import com.google.crypto.tink.StreamingAead
import com.pi.data.persistence.EncryptedDataStoreOptions
import com.pi.data.persistence.serializer.encrypted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Creates DataStore instance stored in [EncryptedFile].
 *
 * Basic usage:
 * ```
 * val dataStore = DataStoreFactory.createEncrypted(serializer) {
 *     EncryptedFile.Builder(
 *          context.dataStoreFile("filename"),
 *          context,
 *          MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
 *          EncryptedFile.FileEncryptionScheme.AES256_GCM_4KB
 *     ).build()
 * }
 * ```
 *
 * Or even simpler, if you use `security-crypto-ktx:1.1.0`:
 * ```
 * val dataStore = DataStoreFactory.createEncrypted(serializer) {
 *     EncryptedFile(
 *         context = context,
 *         file = context.dataStoreFile("filename"),
 *         masterKey = MasterKey(context)
 *     )
 * }
 * ```
 *
 * @see DataStoreFactory.create
 */
fun <T> DataStoreFactory.createEncrypted(
    serializer: Serializer<T>,
    corruptionHandler: ReplaceFileCorruptionHandler<T>? = null,
    migrations: List<DataMigration<T>> = listOf(),
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
    encryptionOptions: EncryptedDataStoreOptions.() -> Unit = {},
    produceFile: () -> EncryptedFile
): DataStore<T> {
    val options = EncryptedDataStoreOptions().also(encryptionOptions)
    val encryptedFile = produceFile()

    val streamingAead = encryptedFile.streamingAead.withDecryptionFallback(options.fallbackAead)
    val file = encryptedFile.file
    val associatedData = options.associatedData ?: file.name.toByteArray()

    return create(
        serializer = serializer.encrypted(streamingAead, associatedData),
        corruptionHandler = corruptionHandler,
        migrations = migrations,
        scope = scope,
        produceFile = { file },
    )
}

private fun StreamingAead.withDecryptionFallback(fallbackAead: Aead?): StreamingAead {
    return if (fallbackAead != null) this.withDecryptionFallback(fallbackAead) else this
}
