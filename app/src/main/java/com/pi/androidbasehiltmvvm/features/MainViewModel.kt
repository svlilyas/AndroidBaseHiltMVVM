package com.pi.androidbasehiltmvvm.features

import com.pi.androidbasehiltmvvm.core.platform.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel()
