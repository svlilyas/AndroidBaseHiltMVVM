package com.pi.androidbasehiltmvvm.core.platform

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.pi.androidbasehiltmvvm.core.network.NetworkConnectivityObserver
import com.pi.data.persistence.EncryptedDataStoreManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ProjectApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        injectMultiDex()
    }

    private fun injectMultiDex() {
        MultiDex.install(this.applicationContext)
    }

    companion object {
        lateinit var appContext: Context

        /**
         * Used for checking internet connectivity statuses
         * Unavailable, Available, Lost and Losing states
         */
        val connectivityObserver: NetworkConnectivityObserver by lazy {
            NetworkConnectivityObserver(appContext)
        }
        val encryptedDataStoreManager: EncryptedDataStoreManager by lazy {
            EncryptedDataStoreManager(appContext)
        }
    }
}
