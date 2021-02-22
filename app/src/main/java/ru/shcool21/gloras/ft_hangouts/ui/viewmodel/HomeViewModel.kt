package ru.shcool21.gloras.ft_hangouts.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.shcool21.gloras.ft_hangouts.data.db.AppDatabase
import ru.shcool21.gloras.ft_hangouts.system.CustomPreferences
import ru.shcool21.gloras.ft_hangouts.ui.model.HomeAction
import ru.shcool21.gloras.ft_hangouts.ui.model.HomeAction.AddContactClickAction
import ru.shcool21.gloras.ft_hangouts.ui.model.HomeAction.ChangeHeaderColorAction
import ru.shcool21.gloras.ft_hangouts.ui.model.HomeAction.InitAction
import ru.shcool21.gloras.ft_hangouts.ui.model.HomeAction.LoadContactsAction
import ru.shcool21.gloras.ft_hangouts.ui.model.HomeAction.OnContactClickAction
import ru.shcool21.gloras.ft_hangouts.ui.model.HomeState
import ru.shcool21.gloras.ft_hangouts.ui.model.HomeState.ColorChange
import ru.shcool21.gloras.ft_hangouts.ui.model.HomeState.EmptyContactsLis
import ru.shcool21.gloras.ft_hangouts.ui.model.HomeState.ShowContacts
import ru.shcool21.gloras.ft_hangouts.ui.navigation.Router

internal class HomeViewModel(
    private val router: Router,
    private val db: AppDatabase,
    private val pref: CustomPreferences
) : ViewModel() {
    private val _state = MutableLiveData<HomeState>()
    val state: LiveData<HomeState> = _state

    private val contactsDao = db.contactsDao()

    fun perform(action: HomeAction) {
        when (action) {
            is InitAction -> onChangeHeaderColor()
            is LoadContactsAction -> onLoadContacts()
            is OnContactClickAction -> onContactClick(action.contactId)
            is AddContactClickAction -> onAddContactClick()
            is ChangeHeaderColorAction -> onChangeHeaderColor(true)
        }
    }

    private fun onLoadContacts() {
        viewModelScope.launch {
            contactsDao.getAll().collect { contacts ->
                if (contacts.isEmpty()) {
                    _state.value = EmptyContactsLis
                } else {
                    _state.value = ShowContacts(contacts)
                }
            }
        }
    }

    private fun onContactClick(contactId: Int) {
        router.openChatScreen(contactId)
    }

    private fun onAddContactClick() {
        router.openProfileScreen()
    }

    private fun onChangeHeaderColor(change: Boolean = false) {
        if (change) pref.changeColor()
        val headerColor = pref.getHeaderColor()
        val itemColor = pref.getItemColor()
        _state.value = ColorChange(headerColor, itemColor)
    }
}
