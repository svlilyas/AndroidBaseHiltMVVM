package com.pi.androidbasehiltmvvm.features.customer.presentation

import android.os.Bundle
import com.pi.androidbasehiltmvvm.R
import com.pi.androidbasehiltmvvm.core.extensions.observeEvent
import com.pi.androidbasehiltmvvm.core.platform.BaseFragment
import com.pi.androidbasehiltmvvm.core.router.PageName
import com.pi.androidbasehiltmvvm.databinding.FragmentCustomerDashboardBinding
import com.pi.androidbasehiltmvvm.features.customer.domain.viewevent.CustomerDashboardViewEvent
import com.pi.androidbasehiltmvvm.features.customer.domain.viewmodel.CustomerDashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerDashboardFragment :
    BaseFragment<FragmentCustomerDashboardBinding, CustomerDashboardViewModel>(
        layoutId = R.layout.fragment_customer_dashboard,
        viewModelClass = CustomerDashboardViewModel::class.java
    ) {
    override fun getScreenKey(): String = PageName.PreLogin.CUSTOMER_DASHBOARD

    override fun onDataBinding() {
        binding.apply {
            viewmodel = viewModel
        }
        observeEvent(viewModel.event, ::onViewEvent)
    }

    private fun onViewEvent(event: CustomerDashboardViewEvent) {
        when (event) {
            CustomerDashboardViewEvent.GenerateQrCode -> {
            }
        }
    }

    companion object {
        fun newInstance(args: Bundle?): CustomerDashboardFragment {
            val fragment = CustomerDashboardFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
