package ru.shcool21.gloras.ft_hangouts.ui.model

import ru.shcool21.gloras.ft_hangouts.data.entity.Contact
import ru.shcool21.gloras.ft_hangouts.data.entity.Message

internal sealed class ChatState {

    data class ShowMessagesState(
        val contact: Contact,
        val messages: List<Message>
    ) : ChatState()

    data class InitState(val headerColor: Int) : ChatState()

}