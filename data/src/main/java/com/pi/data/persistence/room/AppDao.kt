package com.pi.data.persistence.room

import androidx.room.*
import com.pi.data.remote.response.Note
import com.pi.data.remote.response.SampleResponse

@Dao
interface AppDao {

    /**
     * sample_db Operations
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sampleResponse: SampleResponse)

    @Query("SELECT * FROM sample_db WHERE name = :name")
    suspend fun getSampleInfo(name: String): SampleResponse?

    /**
     * note_db Operations
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    /**
     * @param note Updating
     */
    @Update
    suspend fun update(note: Note)

    /**
     * @param note Updating with timestamp which means modified timestamp
     */
    suspend fun updateWithTimeStamp(note: Note) {
        update(
            note.copy(modifiedAt = System.currentTimeMillis())
        )
    }

    /**
     * @param note Deleting
     */
    @Delete
    suspend fun delete(note: Note): Int

    /**
     * Getting all notes in database
     */
    @Query("SELECT * FROM note_db")
    suspend fun getAllNotes(): List<Note>
}
