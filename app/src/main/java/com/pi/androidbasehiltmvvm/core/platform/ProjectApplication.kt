package com.pi.androidbasehiltmvvm.core.platform

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.pi.androidbasehiltmvvm.BuildConfig
import com.pi.androidbasehiltmvvm.core.common.PreferenceManager
import com.pi.androidbasehiltmvvm.core.network.NetworkConnectivityObserver
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ProjectApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        injectMultiDex()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
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
        val preferenceManager: PreferenceManager by lazy {
            PreferenceManager(appContext)
        }
    }
}
