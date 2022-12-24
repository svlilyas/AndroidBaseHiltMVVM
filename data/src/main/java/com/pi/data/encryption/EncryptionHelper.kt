package com.pi.data.encryption

interface EncryptionHelper {
    fun encryptData(rawData: String): String

    fun decryptData(encryptedData: String): String
}