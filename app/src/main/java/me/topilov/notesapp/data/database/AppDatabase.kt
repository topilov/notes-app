package me.topilov.notesapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import me.topilov.notesapp.data.dao.NoteDao
import me.topilov.notesapp.data.entities.Note

@Database(
    entities = [Note::class],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
}