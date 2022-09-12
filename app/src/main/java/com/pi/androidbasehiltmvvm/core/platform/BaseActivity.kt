package com.pi.androidbasehiltmvvm.core.platform

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.result.ActivityResult
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import timber.log.Timber

/**
 * Base activity for managing common functions,
 * to reduce code size and improve code quality
 */
abstract class BaseActivity<DB : ViewDataBinding, VM : ViewModel>(
    @LayoutRes private val layoutId: Int,
    private val viewModelClass: Class<VM>
) : AppCompatActivity() {

    val binding by lazy {
        DataBindingUtil.setContentView(this, layoutId) as DB
    }
    val viewModel by lazy {
        ViewModelProvider(this)[viewModelClass]
    }

    protected val activityLauncher: BetterActivityResult<Intent, ActivityResult> =
        BetterActivityResult.registerActivityForResult(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        supportActionBar?.hide()

        // The window will not be resized when virtual keyboard is shown (bottom navigation bar will be
        // hidden under virtual keyboard)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        Timber.v("onCreate ${javaClass.simpleName}")

        setUpViews()
    }

    open fun setUpViews() {}

    open fun finishApp() {
        finish()
        val finishIntent =
            Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

        startActivity(finishIntent)
    }
}
