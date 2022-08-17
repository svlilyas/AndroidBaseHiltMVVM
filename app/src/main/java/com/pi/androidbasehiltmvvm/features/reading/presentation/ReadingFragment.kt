package com.pi.androidbasehiltmvvm.features.reading.presentation

import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.findNavController
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
        binding.apply {
            when (event) {
                ReadingViewEvent.NavigateToResultPage -> {
                    try {
                        val serviceNumber = serviceNumTextInputEditText.text.toString()
                        val currentReading =
                            currentMeterTextInputEditText.text.toString().toFloat()

                        if (serviceNumber.isEmpty()) {
                            serviceNumTextInputLayout.error = "Please fill here"
                        }
                        if (currentReading.compareTo(0.0F) < 0) {
                            serviceNumTextInputLayout.error =
                                "Please Fill with number bigger than 0"
                        }

                        if (serviceNumber.isNotEmpty() && currentReading > 0) {

                            viewModel.getIfHasRecord(serviceNumber).observe(
                                viewLifecycleOwner,
                            ) {
                                if (it != null) {
                                    if (currentReading.compareTo(it.reading ?: 0f) == 1) {
                                        findNavController().navigate(
                                            ReadingFragmentDirections.actionReadingFragmentToResultFragment(
                                                serviceNumber = serviceNumber,
                                                currentReading = currentReading,
                                                remainingReading = currentReading.minus(
                                                    it.reading ?: 0f
                                                )
                                            )
                                        )
                                    } else {
                                        currentMeterTextInputLayout.error =
                                            "Please type bigger reading than last"
                                    }
                                } else {
                                    findNavController().navigate(
                                        ReadingFragmentDirections.actionReadingFragmentToResultFragment(
                                            serviceNumber = serviceNumber,
                                            currentReading = currentReading,
                                            remainingReading = currentReading
                                        )
                                    )
                                }
                            }


                        }
                    } catch (e: Exception) {
                        Log.d("Exception", "Problem occured ${e.message}")
                    }
                }
            }
        }
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
