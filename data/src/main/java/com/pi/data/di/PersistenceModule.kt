package com.pi.data.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.pi.data.persistence.AppDao
import com.pi.data.persistence.AppDatabase
import com.pi.data.persistence.EncryptedDataStoreManager
import com.pi.data.utils.Constants.Companion.DATASTORE_FILE
import com.pi.data.utils.Constants.Companion.ENCRYPTION_TYPE
import com.pi.data.utils.Constants.Companion.KEYSET_NAME
import com.pi.data.utils.Constants.Companion.MASTER_KEY_URI
import com.pi.data.utils.Constants.Companion.PREFERENCE_FILE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {
    @Singleton
    @Provides
    fun provideAead(application: Application): Aead {
        AeadConfig.register()

        DataStoreFactory
        return AndroidKeysetManager.Builder()
            .withSharedPref(application, KEYSET_NAME, PREFERENCE_FILE)
            .withKeyTemplate(KeyTemplates.get(ENCRYPTION_TYPE)).withMasterKeyUri(MASTER_KEY_URI)
            .build().keysetHandle.getPrimitive(Aead::class.java)
    }

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() }),
            produceFile = { appContext.preferencesDataStoreFile(DATASTORE_FILE) })

    @Singleton
    @Provides
    fun provideEncryptedDataStoreManager(
        dataStore: DataStore<Preferences>,
        aead: Aead
    ): EncryptedDataStoreManager =
        EncryptedDataStoreManager(dataStore = dataStore)

    @Provides
    @Singleton
    fun provideAppDatabase(
        application: Application
    ): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "App.db")
            .fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideAppDao(appDatabase: AppDatabase): AppDao {
        return appDatabase.appDao()
    }
}
