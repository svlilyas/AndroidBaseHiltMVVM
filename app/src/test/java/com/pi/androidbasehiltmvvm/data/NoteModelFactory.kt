package com.pi.androidbasehiltmvvm.data

import com.pi.data.remote.response.Note

object NoteModelFactory {

    private fun getMockNote(): Note = Note(
        id = 0,
        title = "",
        description = "",
        createdAt = 0,
        modifiedAt = 0,
        imageUrl = "",
    )

    fun getMockNotes(): List<Note> {
        val noteList = ArrayList<Note>()
        repeat(5) {
            noteList.add(getMockNote())
        }
        return noteList
    }
}
