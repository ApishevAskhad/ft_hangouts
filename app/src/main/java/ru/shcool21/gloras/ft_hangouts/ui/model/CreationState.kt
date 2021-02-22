package ru.shcool21.gloras.ft_hangouts.ui.model

import ru.shcool21.gloras.ft_hangouts.data.entity.Contact

internal sealed class CreationState {
    data class LoadContact(val contact: Contact) : CreationState()
    object CreationSuccess : CreationState()
    object UpdateSuccess : CreationState()
}
