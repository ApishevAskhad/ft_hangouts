package ru.shcool21.gloras.ft_hangouts.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.widget.Toast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.shcool21.gloras.ft_hangouts.data.db.AppDatabase
import ru.shcool21.gloras.ft_hangouts.data.entity.Contact
import ru.shcool21.gloras.ft_hangouts.data.entity.Message
import javax.inject.Inject

internal class SmsBroadcastReceiver @Inject constructor(
    private val db: AppDatabase
) : BroadcastReceiver() {

    private val contactsDao = db.contactsDao()
    private val messagesDao = db.messagesDao()
    private val jobs = mutableListOf<Job>()

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            Telephony.Sms.Intents.getMessagesFromIntent(intent).forEach { smsMessage ->
                jobs.add(GlobalScope.launch {
                    val phone = smsMessage.displayOriginatingAddress
                    if (!checkContactAndInsert(phone, smsMessage.displayMessageBody)) {
                        contactsDao.insertAll(
                            Contact(
                                name = phone,
                                phoneNumber = phone
                            ))
                        checkContactAndInsert(phone, smsMessage.displayMessageBody)
                    }
                })
                Toast.makeText(context, smsMessage.displayOriginatingAddress, Toast.LENGTH_LONG).show()
            }
        }
    }

    private suspend fun checkContactAndInsert(phone: String, body: String): Boolean {
        return contactsDao.getContactByPhoneNumber(phone)?.let { contact ->
            insertMessage(body, contact.id)
            true
        } ?: false
    }

    private suspend fun insertMessage(body: String, interlocutor: Int) {
        val message = Message(
            body = body,
            time = System.currentTimeMillis().toString(),
            interlocutor = interlocutor,
            isIncoming = true
        )
        messagesDao.insertAll(message)
    }

    /**
     * Should call before unregister() call
     */
    fun onCancel() {
        jobs.forEach { it.cancel() }
    }

}
