package ru.shcool21.gloras.ft_hangouts.ui.navigation

internal interface Router {
    fun setNavigationHandler(navigationHandler: NavigationHandler)
    fun openChatScreen(contactId: Int)
    fun openProfileScreen(contactId: Int? = null)
    fun back()
    fun call(phoneNumber: String)
}