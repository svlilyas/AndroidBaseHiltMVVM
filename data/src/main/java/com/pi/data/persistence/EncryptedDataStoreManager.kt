package com.pi.data.persistence

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pi.data.local.models.ExampleModel
import com.pi.data.local.models.PaymentStatus
import com.pi.data.utils.convertToObject
import com.pi.data.utils.convertToString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException
import javax.inject.Inject

class EncryptedDataStoreManager @Inject constructor(
    val dataStore: DataStore<Preferences>
) {
    inline fun <reified T> getValue(
        key: String, defaultValue: T
    ): Flow<T> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val value = preferences[stringPreferencesKey(key)]
        if (value.isNullOrEmpty()) {
            return@map defaultValue
        }

        convertToObject(value = value) ?: defaultValue
    }

    suspend inline fun <reified T> setValue(key: String, newValue: T) {
        convertToString(value = newValue)?.let { newValueNotNull ->
            dataStore.edit { mutablePreferences ->
                mutablePreferences[stringPreferencesKey(key)] = newValueNotNull
            }
        }
    }

    var paymentStatus: Flow<PaymentStatus>
        get() = getValue(
            key = PAYMENT_STATUS_KEY, defaultValue = PaymentStatus.WAITING
        )
        set(value) {
            runBlocking(Dispatchers.IO) {
                value.collectLatest {
                    setValue(PAYMENT_STATUS_KEY, it)
                }
            }
        }

    var exampleModel: Flow<ExampleModel>
        get() = getValue(
            key = EXAMPLE_MODEL_KEY, defaultValue = ExampleModel()
        )
        set(value) {
            runBlocking(Dispatchers.IO) {
                value.collectLatest {
                    setValue(EXAMPLE_MODEL_KEY, it)
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
