package com.pi.androidbasehiltmvvm.core.platform

import androidx.navigation.NavDirections

sealed class NavigationAction {
    class ToDirection(val direction: NavDirections) : NavigationAction()
    object Back : NavigationAction()
}
