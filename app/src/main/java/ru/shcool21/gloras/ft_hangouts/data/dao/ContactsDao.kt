package ru.shcool21.gloras.ft_hangouts.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.shcool21.gloras.ft_hangouts.data.entity.Contact

@Dao
internal interface ContactsDao {
    @Query("select * from contacts")
    fun getAll(): Flow<List<Contact>>

    @Insert
    suspend fun insertAll(vararg contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)

    @Update
    suspend fun update(contact: Contact)

    @Query("select * from contacts where id like :id")
    suspend fun getContactById(id: Int): Contact?

    @Query("select * from contacts where phone_number like :phoneNumber")
    suspend fun getContactByPhoneNumber(phoneNumber: String): Contact?
}
