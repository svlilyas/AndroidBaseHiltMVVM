package com.pi.androidbasehiltmvvm.core.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pi.androidbasehiltmvvm.core.models.CustomerReading

@Database(entities = [CustomerReading::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAppDao(): AppDao

    companion object {
        private var DB_INSTANCE: AppDatabase? = null

        fun getAppDbInstance(context: Context): AppDatabase {
            if (DB_INSTANCE == null) {
                DB_INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "APP_DB3"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return DB_INSTANCE as AppDatabase
        }
    }
}