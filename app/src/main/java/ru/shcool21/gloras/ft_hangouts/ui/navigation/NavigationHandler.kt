package ru.shcool21.gloras.ft_hangouts.ui.navigation

import androidx.fragment.app.Fragment

internal interface NavigationHandler {
    fun openFragment(fragment: Fragment)
    fun onBack()
    fun onExit()
}
