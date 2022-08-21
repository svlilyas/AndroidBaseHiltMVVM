package com.pi.data.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pi.data.remote.response.SampleResponse

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sampleResponse: SampleResponse)

    @Query("SELECT * FROM sample_db WHERE name = :name")
    suspend fun getSampleInfo(name: String): SampleResponse?
}
