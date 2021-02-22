package ru.shcool21.gloras.ft_hangouts.ui.model

internal sealed class HomeAction {
    object LoadContactsAction : HomeAction()
    data class OnContactClickAction(val contactId: Int) : HomeAction()
    object AddContactClickAction : HomeAction()
    object ChangeHeaderColorAction : HomeAction()
    object InitAction : HomeAction()
}