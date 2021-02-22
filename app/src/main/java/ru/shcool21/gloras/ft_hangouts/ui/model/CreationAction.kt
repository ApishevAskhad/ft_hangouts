package ru.shcool21.gloras.ft_hangouts.ui.model

import ru.shcool21.gloras.ft_hangouts.data.entity.Contact

internal sealed class CreationAction {
    data class LoadContactAction(val contactId: Int) : CreationAction()
    data class CreateContactAction(val contact: Contact): CreationAction()
    data class UpdateContactAction(val contact: Contact): CreationAction()
}