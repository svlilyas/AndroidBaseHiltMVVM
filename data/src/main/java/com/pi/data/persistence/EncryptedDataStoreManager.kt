package com.pi.data.persistence

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pi.data.encryption.EncryptionHelper
import com.pi.data.encryption.EncryptionHelperImpl
import com.pi.data.local.models.ExampleModel
import com.pi.data.local.models.PaymentStatus
import com.pi.data.utils.toStringOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class EncryptedDataStoreManager @Inject constructor(
    val dataStore: DataStore<Preferences>
) {

    inline fun <reified T> getData(
        key: String, defaultValue: T
    ): Flow<T> {
        val classType = T::class.java
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val encryptedValue = preferences[stringPreferencesKey(key)]
            if (encryptedValue.isNullOrEmpty()) {
                return@map defaultValue
            }

            val encryptionHelper: EncryptionHelper = EncryptionHelperImpl()
            val decryptedValue = encryptionHelper.decryptData(encryptedValue)

            Timber.e("Get :: DecryptedValue -> $decryptedValue ${T::class} ${T::class.isData}")

            val exactData: T? = try {
                when (T::class) {
                    Boolean::class -> decryptedValue.toBoolean() as T
                    Float::class -> decryptedValue.toFloat() as T
                    Int::class -> decryptedValue.toInt() as T
                    Long::class -> decryptedValue.toLong() as T
                    String::class -> decryptedValue as T
                    else -> {
                        Gson().fromJson(decryptedValue, classType)
                    }
                }
            } catch (e: Exception) {
                Timber.e(e.message.toString())
                null
            }

            Timber.e("Get :: Result -> ${exactData.toString()}")

            exactData ?: defaultValue
        }
    }

    suspend inline fun <reified T> setData(key: String, newValue: T) {
        val stringValue: String? = when (T::class) {
            Boolean::class -> newValue.toStringOrNull()
            Float::class -> newValue.toStringOrNull()
            Int::class -> newValue.toStringOrNull()
            Long::class -> newValue.toStringOrNull()
            String::class -> newValue.toStringOrNull()
            else -> {
                Gson().toJson(newValue, object : TypeToken<T>() {}.type)
            }
        }

        Timber.e("Set :: Result -> $stringValue")

        stringValue?.let { newValueNotNull ->
            val encryptionHelper: EncryptionHelper = EncryptionHelperImpl()

            val encryptedValue = encryptionHelper.encryptData(newValueNotNull)
            Timber.e("Set :: EncryptedValue -> $encryptedValue")

            dataStore.edit { mutablePreferences ->
                mutablePreferences[stringPreferencesKey(key)] = encryptedValue
            }
        }
    }

    var paymentStatus: Flow<PaymentStatus>
        get() = getData(
            key = PAYMENT_STATUS_KEY, defaultValue = PaymentStatus.WAITING
        )
        set(value) {
            runBlocking(Dispatchers.IO) {
                value.collectLatest {
                    setData(PAYMENT_STATUS_KEY, it)
                }
            }
        }

    var exampleModel: Flow<ExampleModel>
        get() = getData(
            key = EXAMPLE_MODEL_KEY, defaultValue = ExampleModel()
        )
        set(value) {
            runBlocking(Dispatchers.IO) {
                value.collectLatest {
                    setData(EXAMPLE_MODEL_KEY, it)
                }
            }
        }

    suspend fun clear() {
        dataStore.edit { mutablePreferences -> mutablePreferences.clear() }
    }

    companion object {
        private const val PAYMENT_STATUS_KEY = "PAYMENT_STATUS_KEY"
        private const val EXAMPLE_MODEL_KEY = "EXAMPLE_MODEL_KEY"
    }
}
