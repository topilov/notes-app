package me.topilov.notesapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo("title") var title: String = "",
    @ColumnInfo("description") var description: String = "",
    @ColumnInfo("timestamp") var timestamp: Long = 0L,
)
