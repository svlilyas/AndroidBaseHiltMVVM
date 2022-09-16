package com.pi.androidbasehiltmvvm.core.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

fun <T> LifecycleOwner.observeEvent(sharedFlow: SharedFlow<Event<T>>, flow: (T) -> Unit) {
    lifecycleScope.launchWhenStarted {
        sharedFlow.collectLatest {
            it.getContentIfNotHandled()?.let { t -> flow(t) }
        }
    }
}


