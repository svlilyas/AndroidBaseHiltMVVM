package com.pi.androidbasehiltmvvm.features

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pi.androidbasehiltmvvm.R
import com.pi.androidbasehiltmvvm.core.common.PageType
import com.pi.androidbasehiltmvvm.core.platform.BaseActivity
import com.pi.androidbasehiltmvvm.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(
    layoutId = R.layout.activity_main,
    viewModelClass = MainViewModel::class.java
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDataBinding() {
    }

    companion object {
        fun newIntent(context: Context, type: PageType, targetUrl: String = ""): Intent {
            return Intent(context, MainActivity::class.java).apply {
                putExtra("type", type.type)
                putExtra("targetUrl", targetUrl)
            }
        }
    }
}
