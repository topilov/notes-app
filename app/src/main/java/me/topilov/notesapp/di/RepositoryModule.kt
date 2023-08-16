package me.topilov.notesapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import me.topilov.notesapp.data.repositories.NoteRepositoryImpl
import me.topilov.notesapp.domain.repositories.NoteRepository

@InstallIn(ActivityRetainedComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun providesNoteRepository(impl: NoteRepositoryImpl): NoteRepository
}