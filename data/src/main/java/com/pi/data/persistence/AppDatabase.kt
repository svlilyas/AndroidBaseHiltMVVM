package com.pi.data.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pi.data.remote.response.SampleResponse

@Database(entities = [SampleResponse::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao
}
