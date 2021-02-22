package ru.shcool21.gloras.ft_hangouts.ui.viewmodel

import android.telephony.SmsManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.shcool21.gloras.ft_hangouts.data.db.AppDatabase
import ru.shcool21.gloras.ft_hangouts.data.entity.Contact
import ru.shcool21.gloras.ft_hangouts.data.entity.Message
import ru.shcool21.gloras.ft_hangouts.system.CustomPreferences
import ru.shcool21.gloras.ft_hangouts.ui.model.ChatAction
import ru.shcool21.gloras.ft_hangouts.ui.model.ChatAction.BackClickAction
import ru.shcool21.gloras.ft_hangouts.ui.model.ChatAction.CallContactAction
import ru.shcool21.gloras.ft_hangouts.ui.model.ChatAction.DeleteContactAction
import ru.shcool21.gloras.ft_hangouts.ui.model.ChatAction.EditContactAction
import ru.shcool21.gloras.ft_hangouts.ui.model.ChatAction.InitAction
import ru.shcool21.gloras.ft_hangouts.ui.model.ChatAction.LoadTextMessagesAction
import ru.shcool21.gloras.ft_hangouts.ui.model.ChatAction.SendTextMessageAction
import ru.shcool21.gloras.ft_hangouts.ui.model.ChatState
import ru.shcool21.gloras.ft_hangouts.ui.model.ChatState.InitState
import ru.shcool21.gloras.ft_hangouts.ui.model.ChatState.ShowMessagesState
import ru.shcool21.gloras.ft_hangouts.ui.navigation.Router
import javax.inject.Inject

internal class ChatViewModel @Inject constructor(
    private val router: Router,
    private val db: AppDatabase,
    private val pref: CustomPreferences
) : ViewModel() {
    private val _state = MutableLiveData<ChatState>()
    val state: LiveData<ChatState> = _state

    private val contactsDao = db.contactsDao()
    private val messagesDao = db.messagesDao()
    private var contact: Contact? = null

    fun perform(action: ChatAction) {
        when (action) {
            is InitAction -> onInit()
            is LoadTextMessagesAction -> onLoadTextMessages(action.contactId)
            is SendTextMessageAction -> onSendTextMessage(action.text)
            is BackClickAction -> onBackClick()
            is CallContactAction -> onCallContact()
            is EditContactAction -> onEditContact()
            is DeleteContactAction -> onDeleteContact()
        }
    }

    private fun onInit() {
        val headerColor = pref.getHeaderColor()
        _state.value = InitState(headerColor)
    }

    private fun onLoadTextMessages(contactId: Int) {
        viewModelScope.launch {
            contact = contactsDao.getContactById(contactId)
            contact?.let { contact ->
                messagesDao.getAllFromContactById(contact.id)
                    .collect { messages ->
                        //TODO sort messages by time
                        _state.value = ShowMessagesState(contact, messages)
                    }
            }
        }
    }

    //todo read https://stackoverflow.com/questions/2480288/programmatically-obtain-the-phone-number-of-the-android-phone
    private fun onSendTextMessage(text: String) {
        viewModelScope.launch {
            contact?.let {
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(it.phoneNumber, null, text, null, null)
                val message = Message(
                    body = text,
                    time = System.currentTimeMillis().toString(),
                    interlocutor = it.id,
                    isIncoming = false
                )
                messagesDao.insertAll(message)
                onLoadTextMessages(it.id)
            }
        }
    }

    private fun onBackClick() {
        router.back()
    }

    private fun onCallContact() {
        contact?.let { router.call(it.phoneNumber) }
    }

    private fun onEditContact() {
        contact?.let { router.openProfileScreen(it.id) }
    }

    private fun onDeleteContact() {
        viewModelScope.launch {
            contact?.let {
                contactsDao.delete(it)
                router.back()
            }
        }
    }

}