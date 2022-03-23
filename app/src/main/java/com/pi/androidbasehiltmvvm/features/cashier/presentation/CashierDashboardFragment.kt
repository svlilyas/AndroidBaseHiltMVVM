package com.pi.androidbasehiltmvvm.features.cashier.presentation

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.pi.androidbasehiltmvvm.R
import com.pi.androidbasehiltmvvm.core.common.PermissionHelper
import com.pi.androidbasehiltmvvm.core.common.permissionmanager.PermissionHandler
import com.pi.androidbasehiltmvvm.core.extensions.observeEvent
import com.pi.androidbasehiltmvvm.core.platform.BaseFragment
import com.pi.androidbasehiltmvvm.core.router.PageName
import com.pi.androidbasehiltmvvm.databinding.CustomDialogBinding
import com.pi.androidbasehiltmvvm.databinding.FragmentCashierDashboardBinding
import com.pi.androidbasehiltmvvm.features.cashier.domain.viewevent.CashierDashboardViewEvent
import com.pi.androidbasehiltmvvm.features.cashier.domain.viewmodel.CashierDashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CashierDashboardFragment :
    BaseFragment<FragmentCashierDashboardBinding, CashierDashboardViewModel>(
        layoutId = R.layout.fragment_cashier_dashboard,
        viewModelClass = CashierDashboardViewModel::class.java
    ) {
    override fun getScreenKey(): String = PageName.PreLogin.CASHIER_DASHBOARD

    override fun onDataBinding() {
        binding.apply {
            viewmodel = viewModel
        }
        observeEvent(viewModel.event, ::onViewEvent)

        fetchViewData()

        requestPermissions()
    }

    private fun fetchViewData() {
    }

    private fun requestPermissions() = PermissionHandler.requestPermission(
        this,
        123,
        PermissionHelper.cameraPermission
    ) {
        permissionsGranted()
    }

    private fun permissionsGranted() {
    }

    private fun showPaymentDialog() {
        val dialogBinding: CustomDialogBinding =
            CustomDialogBinding.inflate(LayoutInflater.from(context))

        dialogBinding.viewmodel = viewModel

        val dialog =
            AlertDialog.Builder(requireContext()).setView(dialogBinding.root).setCancelable(true)

        dialog.show()
    }

    private fun onViewEvent(event: CashierDashboardViewEvent) {
        when (event) {
            CashierDashboardViewEvent.ReadQrCode -> {
            }
        }
    }

    companion object {
        fun newInstance(args: Bundle?): CashierDashboardFragment {
            val fragment = CashierDashboardFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
