package com.pi.data.persistence.factory

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.security.crypto.EncryptedFile
import com.pi.data.persistence.EncryptedDataStoreOptions
import com.pi.data.persistence.serializer.PreferencesSerializer
import com.pi.data.utils.file
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Creates Preferences DataStore instance stored in [EncryptedFile].
 * The file must have the extension "preferences_pb".
 *
 * Basic usage:
 * ```
 * val dataStore = PreferenceDataStoreFactory.createEncrypted {
 *     EncryptedFile.Builder(
 *          context.dataStoreFile("filename.preferences_pb"),
 *          context,
 *          MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
 *          EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
 *     ).build()
 * }
 * ```
 *
 * Or even simpler, if you use `security-crypto-ktx:1.1.0`:
 * ```
 * val dataStore = PreferenceDataStoreFactory.createEncrypted {
 *     EncryptedFile(
 *         context = context,
 *         file = context.dataStoreFile("filename.preferences_pb"),
 *         masterKey = MasterKey(context)
 *     )
 * }
 * ```
 *
 * @see PreferenceDataStoreFactory.create
 */
fun PreferenceDataStoreFactory.createEncrypted(
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
    migrations: List<DataMigration<Preferences>> = listOf(),
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
    encryptionOptions: EncryptedDataStoreOptions.() -> Unit = {},
    produceFile: () -> EncryptedFile,
): DataStore<Preferences> = DataStoreFactory.createEncrypted(
    serializer = PreferencesSerializer,
    corruptionHandler = corruptionHandler,
    migrations = migrations,
    scope = scope,
    encryptionOptions = encryptionOptions,
    produceFile = produceFile()::checkPreferenceDataStoreFileExtension,
)

private const val FILE_EXTENSION = "preferences_pb"

private fun EncryptedFile.checkPreferenceDataStoreFileExtension(): EncryptedFile = apply {
    check(file.extension == FILE_EXTENSION) {
        "File extension for file: $this does not match required extension for Preferences file: $FILE_EXTENSION"
    }
}
