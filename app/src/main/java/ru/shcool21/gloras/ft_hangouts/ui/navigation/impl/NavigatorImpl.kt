package ru.shcool21.gloras.ft_hangouts.ui.navigation.impl

import androidx.fragment.app.Fragment
import ru.shcool21.gloras.ft_hangouts.ui.navigation.Navigation
import ru.shcool21.gloras.ft_hangouts.ui.navigation.NavigationHandler
import java.lang.ref.WeakReference
import javax.inject.Inject

internal class NavigatorImpl @Inject constructor() : Navigation {

    private var navigationHandler: WeakReference<NavigationHandler> = WeakReference(null)

    override fun setNavigationHandler(navigationHandler: NavigationHandler) {
        this.navigationHandler = WeakReference(navigationHandler)
    }

    override fun openFragment(fragment: Fragment) {
        navigationHandler.get()?.openFragment(fragment)
    }

    override fun onBack() {
        navigationHandler.get()?.onBack()
    }

    override fun onExit() {
        navigationHandler.get()?.onExit()
    }

}