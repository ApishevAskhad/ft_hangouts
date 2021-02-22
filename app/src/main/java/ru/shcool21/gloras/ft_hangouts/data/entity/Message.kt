package ru.shcool21.gloras.ft_hangouts.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "messages", foreignKeys = [
    ForeignKey(
        entity = Contact::class,
        parentColumns = ["id"],
        childColumns = ["interlocutor"],
        onDelete = ForeignKey.CASCADE
    )
])
internal data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val body: String,
    val time: String,
    val interlocutor: Int,
    @ColumnInfo(name = "is_incoming") val isIncoming: Boolean
)