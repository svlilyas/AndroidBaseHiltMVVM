package com.pi.data.utils

class Constants {
    companion object {
        const val KEYSET_NAME = "master_keyset"
        const val PREFERENCE_FILE = "master_key_preference"
        const val MASTER_KEY_URI = "android-keystore://master_key"

        const val DATASTORE_FILE = "datastore.pb"
        const val ENCRYPTION_TYPE = "AES256_GCM"
    }
}