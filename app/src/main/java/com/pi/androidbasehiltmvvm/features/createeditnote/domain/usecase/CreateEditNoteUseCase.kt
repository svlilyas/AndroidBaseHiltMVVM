package com.pi.androidbasehiltmvvm.features.createeditnote.domain.usecase

import com.pi.data.remote.response.Note
import com.pi.data.repository.MainRepository
import javax.inject.Inject

class CreateEditNoteUseCase @Inject constructor(private val mainRepository: MainRepository) {

    fun insertNote(note: Note) =
        mainRepository.insertNote(note = note) { }

    fun updateNote(note: Note) =
        mainRepository.updateNote(note = note) { }

    fun deleteNote(note: Note) = mainRepository.deleteNote(note = note) {}
    fun getAllNotes() = mainRepository.getAllNotes {}
}
