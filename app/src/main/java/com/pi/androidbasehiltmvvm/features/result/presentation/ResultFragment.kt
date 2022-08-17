package com.pi.androidbasehiltmvvm.features.result.presentation

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pi.androidbasehiltmvvm.R
import com.pi.androidbasehiltmvvm.core.extensions.observeEvent
import com.pi.androidbasehiltmvvm.core.navigation.PageName
import com.pi.androidbasehiltmvvm.core.platform.BaseFragment
import com.pi.androidbasehiltmvvm.databinding.FragmentResultBinding
import com.pi.androidbasehiltmvvm.features.result.domain.viewevent.ResultViewEvent
import com.pi.androidbasehiltmvvm.features.result.domain.viewmodel.ResultViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ResultFragment : BaseFragment<FragmentResultBinding, ResultViewModel>(
    layoutId = R.layout.fragment_result,
    viewModelClass = ResultViewModel::class.java
) {
    private val args: ResultFragmentArgs by navArgs()

    override fun getScreenKey(): String = PageName.PreLogin.HOMEPAGE_MAIN

    override fun setUpViews() {
        super.setUpViews()
        binding.apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun observeData() {
        super.observeData()
        observeEvent(viewModel.event, ::onViewEvent)

        viewModel.calculateCost(args.serviceNumber, args.remainingReading, args.currentReading)

        Timber.d("${args.serviceNumber} ${args.currentReading}")
    }

    private fun onViewEvent(event: ResultViewEvent) {
        when (event) {
            ResultViewEvent.NavigateReadingPage -> {
                findNavController().navigate(ResultFragmentDirections.actionResultFragmentToReadingFragment())
            }
        }
    }

    companion object {
        fun newInstance(args: Bundle?): ResultFragment {
            val fragment = ResultFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
