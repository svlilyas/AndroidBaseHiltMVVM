package com.pi.data.persistence.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pi.data.remote.response.Note
import com.pi.data.remote.response.SampleResponse

@Database(entities = [SampleResponse::class, Note::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao
}
