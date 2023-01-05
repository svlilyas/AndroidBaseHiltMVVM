package com.pi.data.encryption

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties.*
import androidx.annotation.RequiresApi
import java.security.KeyStore
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import kotlin.concurrent.withLock

class EncryptionHelperImpl : EncryptionHelper {
    private val charset = Charsets.UTF_8
    private val cipher = Cipher.getInstance(TRANSFORMATION)
    private val secretKey: SecretKey = getOrCreateSecretKey()
    private val lock = ReentrantLock(FAIR_ORDERING_POLICY)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun encryptData(rawData: String): String = lock.withLock {

        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val ivString = Base64.getEncoder().encode(cipher.iv).toString(charset)
        val encryptedDataByteArray = cipher.doFinal(rawData.toByteArray(charset))
        val encryptedDataString =
            Base64.getEncoder().encode(encryptedDataByteArray).toString(charset)

        ivString + IV_SEPARATOR + encryptedDataString
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun decryptData(encryptedData: String): String = lock.withLock {
        val split = encryptedData.split(IV_SEPARATOR.toRegex())

        if (split.size != ENCRYPTED_DATA_PARTS_COUNT) {
            throw IllegalArgumentException("Passed data is incorrect. There was no IV specified with it.")
        }

        val ivByteArray = Base64.getDecoder().decode(split[IV_PART_INDEX])
        val ivSpec = IvParameterSpec(ivByteArray)

        val encryptedDataByteArray = Base64.getDecoder().decode(split[ENCRYPTED_DATA_PART_INDEX])

        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)

        cipher.doFinal(encryptedDataByteArray).toString(charset)
    }

    private fun getOrCreateSecretKey(): SecretKey {
        return getSecretKey() ?: createSecretKey()
    }

    private fun createSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM_AES, PROVIDER)
        val builder = KeyGenParameterSpec.Builder(KEY_ALIAS, PURPOSE_ENCRYPT or PURPOSE_DECRYPT)
            .setBlockModes(BLOCK_MODE_CBC)
            .setEncryptionPaddings(ENCRYPTION_PADDING_PKCS7)
        keyGenerator.init(builder.build())
        return keyGenerator.generateKey()
    }

    private fun getSecretKey(): SecretKey? {
        val keyStore = KeyStore.getInstance(PROVIDER).apply { load(null) }
        return (keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry)?.secretKey
    }

    companion object {
        private const val PROVIDER = "AndroidKeyStore"
        private const val TRANSFORMATION = "AES/CBC/PKCS7Padding"
        private const val KEY_ALIAS = "EncryptedDataStoreAlias"
        private const val IV_SEPARATOR = "]"
        private const val IV_PART_INDEX = 0
        private const val ENCRYPTED_DATA_PART_INDEX = 1
        private const val ENCRYPTED_DATA_PARTS_COUNT = 2
        private const val FAIR_ORDERING_POLICY = true
    }
}