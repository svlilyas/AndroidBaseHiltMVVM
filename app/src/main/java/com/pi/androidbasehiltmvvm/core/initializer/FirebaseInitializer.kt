package com.pi.androidbasehiltmvvm.core.initializer

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.FirebaseApp
import timber.log.Timber

class FirebaseInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        Timber.d("FirebaseInitializer is initialized")

        FirebaseApp.initializeApp(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
