package com.pi.androidbasehiltmvvm.core.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
fun <T, K> StateFlow<T>.mapState(
    scope: CoroutineScope,
    transform: (data: T) -> K
): StateFlow<K> {
    return mapLatest {
        transform(it)
    }.stateIn(scope, SharingStarted.Eagerly, transform(value))
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T, K> StateFlow<T>.mapState(
    scope: CoroutineScope,
    initialValue: K,
    transform: suspend (data: T) -> K
): StateFlow<K> {
    return mapLatest {
        transform(it)
    }.stateIn(scope, SharingStarted.Eagerly, initialValue)
}

fun <T> LifecycleOwner.observe(stateFlow: StateFlow<T>, flow: (T) -> Unit) {
    lifecycleScope.launchWhenStarted {
        stateFlow.collectLatest {
            it.let(flow)
        }
    }
}

fun <T> LifecycleOwner.observe(flowData: Flow<T>, flow: (T) -> Unit) {
    lifecycleScope.launchWhenStarted {
        flowData.collectLatest {
            it.let(flow)
        }
    }
}
