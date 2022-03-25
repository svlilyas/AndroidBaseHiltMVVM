package com.pi.androidbasehiltmvvm.features

import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.pi.androidbasehiltmvvm.R
import com.pi.androidbasehiltmvvm.core.extensions.navigateSafe
import com.pi.androidbasehiltmvvm.core.navigation.NavManager
import com.pi.androidbasehiltmvvm.core.platform.BaseActivity
import com.pi.androidbasehiltmvvm.databinding.ActivityNavHostBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NavHostActivity : BaseActivity<ActivityNavHostBinding, MainViewModel>(
    layoutId = R.layout.activity_nav_host,
    viewModelClass = MainViewModel::class.java
) {
    private val navController get() = Navigation.findNavController(this, R.id.navHostFragment)

    @Inject
    lateinit var navManager: NavManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initBottomNavigation()
        initNavManager()
    }

    override fun setUpViews() {
        super.setUpViews()
    }

    private fun initBottomNavigation() {
        binding.bottomNav.setupWithNavController(navController)

        // Disables reselection of bottom menu item, so fragments are not recreated when clicking on the same menu item
        binding.bottomNav.setOnNavigationItemReselectedListener { }
    }

    private fun initNavManager() {
        navManager.setOnNavEvent {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment)
            val currentFragment = navHostFragment?.childFragmentManager?.fragments?.get(0)

            currentFragment?.navigateSafe(it)
        }
    }
}
