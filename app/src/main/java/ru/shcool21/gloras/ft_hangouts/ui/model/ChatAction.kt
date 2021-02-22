package ru.shcool21.gloras.ft_hangouts.ui.model

internal sealed class ChatAction {
    object InitAction : ChatAction()
    data class LoadTextMessagesAction(val contactId: Int) : ChatAction()
    data class SendTextMessageAction(val text: String) : ChatAction()
    object BackClickAction : ChatAction()
    object CallContactAction : ChatAction()
    object EditContactAction : ChatAction()
    object DeleteContactAction : ChatAction()
}