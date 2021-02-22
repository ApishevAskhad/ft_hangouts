package ru.shcool21.gloras.ft_hangouts.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.shcool21.gloras.ft_hangouts.data.dao.ContactsDao
import ru.shcool21.gloras.ft_hangouts.data.dao.MessagesDao
import ru.shcool21.gloras.ft_hangouts.data.entity.Contact
import ru.shcool21.gloras.ft_hangouts.data.entity.Message

@Database(entities = [Contact::class, Message::class], version = 1)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun contactsDao(): ContactsDao
    abstract fun messagesDao(): MessagesDao
}