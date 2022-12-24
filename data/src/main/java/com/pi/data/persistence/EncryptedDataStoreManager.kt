package com.pi.data.persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pi.data.encryption.EncryptionHelper
import com.pi.data.encryption.EncryptionHelperImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class EncryptedDataStoreManager @Inject constructor(
    private val context: Context,
    private val encryptionHelper: EncryptionHelper = EncryptionHelperImpl(),
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = DATA_STORE_NAME, scope = scope
    )

    //region get data

    fun <T> getData(key: String, defaultValue: T): Flow<T> =
        getString(stringPreferencesKey(key)).map { data ->
            Gson().fromJson(data, object : TypeToken<T>() {}.type) ?: defaultValue
        }

    private fun getString(key: Preferences.Key<String>): Flow<String?> {
        return context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val encryptedValue = preferences[key]
            if (encryptedValue.isNullOrEmpty()) {
                return@map null
            }
            encryptionHelper.decryptData(encryptedValue)
        }
    }
    //endregion

    //region put data

    suspend fun <T> setData(key: String, newValue: T) {
        putString(stringPreferencesKey(key), Gson().toJson(newValue))
    }

    private suspend fun putString(key: Preferences.Key<String>, newValue: String?) {
        newValue?.let { newValueNotNull ->
            val encryptedValue = encryptionHelper.encryptData(newValueNotNull)
            context.dataStore.edit { mutablePreferences ->
                mutablePreferences[key] = encryptedValue
            }
        }
    }
    //endregion

    suspend fun clear() {
        context.dataStore.edit { mutablePreferences -> mutablePreferences.clear() }
    }

    companion object {
        private const val DATA_STORE_NAME = "EncryptedDataStorePreferences"
    }
}
