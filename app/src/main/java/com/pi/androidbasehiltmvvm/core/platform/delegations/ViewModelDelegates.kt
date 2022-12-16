package com.pi.androidbasehiltmvvm.core.platform.delegations

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.fragment.findNavController
import com.pi.androidbasehiltmvvm.core.extensions.observe
import com.pi.androidbasehiltmvvm.core.platform.NavigationAction
import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewModel
import timber.log.Timber

@MainThread
inline fun <reified VM : BaseViewModel<*, *>> Fragment.viewModelWithNavigation(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val owner by lazy(LazyThreadSafetyMode.NONE) { ownerProducer() }
    val lazyVM = createViewModelLazy(VM::class, { owner.viewModelStore }, {
        extrasProducer?.invoke()
            ?: (owner as? HasDefaultViewModelProviderFactory)?.defaultViewModelCreationExtras
            ?: CreationExtras.Empty
    }, factoryProducer ?: {
        (owner as? HasDefaultViewModelProviderFactory)?.defaultViewModelProviderFactory
            ?: defaultViewModelProviderFactory
    })

    /**
     * Listening Navigation calls from [BaseViewModel] which it means from all ViewModels
     */
    val navigationObserver = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                observe(lazyVM.value.navigation) {
                    it.getContentIfNotHandled()?.let { navAction ->
                        when (navAction) {
                            is NavigationAction.ToDirection -> findNavController().navigate(
                                navAction.direction
                            )
                            NavigationAction.Back -> findNavController().navigateUp()
                        }
                        Timber.d("Navigation Action -> $navAction")
                    }
                }
            }
            else -> Unit
        }
    }

    /** Adding observer to [Lifecycle] */
    lifecycle.addObserver(navigationObserver)

    return lazyVM
}


