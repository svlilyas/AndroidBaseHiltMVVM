package com.pi.androidbasehiltmvvm.features.reading.presentation

import android.os.Bundle
import com.pi.androidbasehiltmvvm.R
import com.pi.androidbasehiltmvvm.core.extensions.observeEvent
import com.pi.androidbasehiltmvvm.core.navigation.PageName
import com.pi.androidbasehiltmvvm.core.platform.BaseFragment
import com.pi.androidbasehiltmvvm.databinding.FragmentReadingBinding
import com.pi.androidbasehiltmvvm.features.reading.domain.viewevent.ReadingViewEvent
import com.pi.androidbasehiltmvvm.features.reading.domain.viewmodel.ReadingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReadingFragment :
    BaseFragment<FragmentReadingBinding, ReadingViewModel>(
        layoutId = R.layout.fragment_reading,
        viewModelClass = ReadingViewModel::class.java
    ) {
    override fun getScreenKey(): String = PageName.PreLogin.CASHIER_DASHBOARD

    override fun setUpViews() {
        super.setUpViews()
        binding.apply {
            viewmodel = viewModel
        }
    }

    override fun observeData() {
        super.observeData()
        observeEvent(viewModel.event, ::onViewEvent)
    }

    private fun onViewEvent(event: ReadingViewEvent) {

    }

    override fun onResume() {
        super.onResume()
        binding.apply {
            currentMeterTextInputLayout.error = ""
            serviceNumTextInputLayout.error = ""
        }
    }

    companion object {
        fun newInstance(args: Bundle?): ReadingFragment {
            val fragment = ReadingFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
