package ru.shcool21.gloras.ft_hangouts.ui.navigation.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.shcool21.gloras.ft_hangouts.ui.fragment.ChatFragment
import ru.shcool21.gloras.ft_hangouts.ui.fragment.CreationFragment
import ru.shcool21.gloras.ft_hangouts.ui.navigation.Navigation
import ru.shcool21.gloras.ft_hangouts.ui.navigation.NavigationHandler
import ru.shcool21.gloras.ft_hangouts.ui.navigation.Router
import javax.inject.Inject

internal class RouterImpl @Inject constructor(
    private val navigator: Navigation,
    private val context: Context
) : Router {

    override fun setNavigationHandler(navigationHandler: NavigationHandler) {
        navigator.setNavigationHandler(navigationHandler)
    }

    override fun openChatScreen(contactId: Int) {
        navigator.openFragment(ChatFragment.newInstance(contactId))
    }

    override fun openProfileScreen(contactId: Int?) {
        contactId?.let {
            navigator.openFragment(CreationFragment.newInstance(it))
        } ?: navigator.openFragment(CreationFragment())
    }

    override fun back() {
        navigator.onBack()
    }

    override fun call(phoneNumber: String) {
        val callIntent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(callIntent)
    }

}