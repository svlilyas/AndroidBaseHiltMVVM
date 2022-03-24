package com.pi.androidbasehiltmvvm.features.homepage.presentation

import android.os.Bundle
import com.pi.androidbasehiltmvvm.R
import com.pi.androidbasehiltmvvm.core.extensions.observeEvent
import com.pi.androidbasehiltmvvm.core.navigation.PageName
import com.pi.androidbasehiltmvvm.core.platform.BaseFragment
import com.pi.androidbasehiltmvvm.databinding.FragmentHomepageBinding
import com.pi.androidbasehiltmvvm.features.homepage.domain.viewevent.HomePageViewEvent
import com.pi.androidbasehiltmvvm.features.homepage.domain.viewmodel.HomePageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePageFragment : BaseFragment<FragmentHomepageBinding, HomePageViewModel>(
    layoutId = R.layout.fragment_homepage,
    viewModelClass = HomePageViewModel::class.java
) {
    override fun getScreenKey(): String = PageName.PreLogin.HOMEPAGE_MAIN

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

    private fun onViewEvent(event: HomePageViewEvent) {
        when (event) {
            HomePageViewEvent.NavigateCashierPage -> {
            }
            HomePageViewEvent.NavigateCustomerPage -> {
            }
        }
    }

    companion object {
        fun newInstance(args: Bundle?): HomePageFragment {
            val fragment = HomePageFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
