package ru.shcool21.gloras.ft_hangouts.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.shcool21.gloras.ft_hangouts.data.entity.Message

@Dao
internal interface MessagesDao {
    @Query("select * from messages where interlocutor like :contactId")
    fun getAllFromContactById(contactId: Int): Flow<List<Message>>

    @Insert
    suspend fun insertAll(vararg message: Message)
}