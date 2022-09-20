package com.pi.androidbasehiltmvvm.core.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

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

fun <T> Flow<T>.launchWhenStarted(lifecycleOwner: LifecycleOwner, flow: (T) -> Unit) =
    with(lifecycleOwner) {
        // Start a coroutine in the lifecycle scope
        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Trigger the flow and start listening for values.
                // Note that this happens when lifecycle is STARTED and stops
                // collecting when the lifecycle is STOPPED
                try {
                    this@launchWhenStarted.collectLatest {
                        // New value received
                        it.let(flow)
                    }
                } catch (t: Throwable) {
                    Timber.e(t)
                }
            }
        }
    }

fun <T> LifecycleOwner.observe(stateFlow: StateFlow<T>, flow: (T) -> Unit) {
    /**
     * Start a coroutine in the lifecycle scope
     */
    lifecycleScope.launch {
        /**
         * repeatOnLifecycle launches the block in a new coroutine every time the
         * lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
         */
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            /**
             * Trigger the flow and start listening for values.
             * Note that this happens when lifecycle is STARTED and stops
             * collecting when the lifecycle is STOPPED
             */
            try {
                stateFlow.collectLatest {
                    // New value received
                    it?.let(flow)
                }
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }
}

fun <T> LifecycleOwner.observe(flowData: Flow<T>, flow: (T) -> Unit) {
    this.lifecycleScope.launchWhenStarted {
        flowData.collectLatest {
            it.let(flow)
        }
    }
}
