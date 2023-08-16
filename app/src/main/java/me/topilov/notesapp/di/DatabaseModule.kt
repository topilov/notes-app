package me.topilov.notesapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.topilov.notesapp.data.database.AppDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun provideNoteDao(appDatabase: AppDatabase) = appDatabase.noteDao()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context) = Room.databaseBuilder(
        context = appContext,
        klass = AppDatabase::class.java,
        name = "notes_app.db",
    ).fallbackToDestructiveMigration().build()
}