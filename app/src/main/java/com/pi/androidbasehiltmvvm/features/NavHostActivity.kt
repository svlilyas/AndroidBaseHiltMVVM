package com.pi.androidbasehiltmvvm.features

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.pi.androidbasehiltmvvm.R
import com.pi.androidbasehiltmvvm.core.extensions.navigateSafe
import com.pi.androidbasehiltmvvm.core.extensions.observe
import com.pi.androidbasehiltmvvm.core.navigation.NavManager
import com.pi.androidbasehiltmvvm.core.network.ConnectivityObserver
import com.pi.androidbasehiltmvvm.core.platform.ProjectApplication.Companion.connectivityObserver
import com.pi.androidbasehiltmvvm.core.platform.delegations.viewBinding
import com.pi.androidbasehiltmvvm.databinding.ActivityNavHostBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NavHostActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityNavHostBinding::inflate)
    private val viewModel by viewModels<MainViewModel>()

    private val navController get() = findNavController(R.id.navHostFragment)

    @Inject
    lateinit var navManager: NavManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initNavManager()
        setUpViews()
    }

    private fun setUpViews() {
        observe(connectivityObserver.observe()) { connectionStatus ->
            val status = when (connectionStatus) {
                ConnectivityObserver.ConnectionStatus.Available -> "Available"
                ConnectivityObserver.ConnectionStatus.Unavailable -> "Unavailable"
                ConnectivityObserver.ConnectionStatus.Losing -> "Losing"
                ConnectivityObserver.ConnectionStatus.Lost -> "Lost"
            }

            Toast.makeText(this@NavHostActivity, status, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initNavManager() {
        navManager.setOnNavEvent {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment)
            val currentFragment = navHostFragment?.childFragmentManager?.fragments?.get(0)

            currentFragment?.navigateSafe(it)
        }
    }
}
