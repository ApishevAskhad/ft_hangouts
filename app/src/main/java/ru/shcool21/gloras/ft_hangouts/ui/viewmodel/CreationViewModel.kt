package ru.shcool21.gloras.ft_hangouts.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.shcool21.gloras.ft_hangouts.data.db.AppDatabase
import ru.shcool21.gloras.ft_hangouts.data.entity.Contact
import ru.shcool21.gloras.ft_hangouts.ui.model.CreationAction
import ru.shcool21.gloras.ft_hangouts.ui.model.CreationAction.CreateContactAction
import ru.shcool21.gloras.ft_hangouts.ui.model.CreationAction.LoadContactAction
import ru.shcool21.gloras.ft_hangouts.ui.model.CreationAction.UpdateContactAction
import ru.shcool21.gloras.ft_hangouts.ui.model.CreationState
import ru.shcool21.gloras.ft_hangouts.ui.model.CreationState.CreationSuccess
import ru.shcool21.gloras.ft_hangouts.ui.model.CreationState.LoadContact
import ru.shcool21.gloras.ft_hangouts.ui.model.CreationState.UpdateSuccess
import ru.shcool21.gloras.ft_hangouts.ui.navigation.Router
import javax.inject.Inject

internal class CreationViewModel @Inject constructor(
    private val router: Router,
    private val db: AppDatabase
) : ViewModel() {
    private val _state = MutableLiveData<CreationState>()
    val state: LiveData<CreationState> = _state

    private val contactDao = db.contactsDao()
    private var contact: Contact? = null

    fun perform(action: CreationAction) {
        when (action) {
            is LoadContactAction -> onLoadContact(action.contactId)
            is CreateContactAction -> onCreateContact(action.contact)
            is UpdateContactAction -> onUpdateContact(action.contact)
        }
    }

    private fun onLoadContact(contactId: Int) {
        viewModelScope.launch {
            contact = contactDao.getContactById(contactId)
            contact?.let { _state.value = LoadContact(it) }
        }
    }

    private fun onCreateContact(contact: Contact) {
        viewModelScope.launch {
            contactDao.insertAll(contact)
            _state.value = CreationSuccess
            router.back()
        }
    }

    //contact id property must be setted
    private fun onUpdateContact(contact: Contact) {
        viewModelScope.launch {
            contactDao.update(contact)
            _state.value = UpdateSuccess
            router.back()
        }
    }
}
