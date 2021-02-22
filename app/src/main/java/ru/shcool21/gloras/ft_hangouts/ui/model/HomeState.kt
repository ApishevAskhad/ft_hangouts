package ru.shcool21.gloras.ft_hangouts.ui.model

import ru.shcool21.gloras.ft_hangouts.data.entity.Contact

internal sealed class HomeState {
    data class ShowContacts(val contacts: List<Contact>) : HomeState()
    object EmptyContactsLis : HomeState()
    data class ColorChange(val headerColor: Int, val itemRes: Int) : HomeState()
}