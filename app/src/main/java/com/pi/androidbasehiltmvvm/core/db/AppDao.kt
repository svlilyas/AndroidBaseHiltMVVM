package com.pi.androidbasehiltmvvm.core.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pi.androidbasehiltmvvm.core.models.CustomerReading

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(customerReading: CustomerReading)

    @Update
    fun update(customerReading: CustomerReading)

    @Delete
    fun delete(customerReading: CustomerReading)

    @Query("SELECT * FROM customer_readings")
    fun getAll(): LiveData<List<CustomerReading>>

    @Query("SELECT * FROM customer_readings WHERE id=:customerId ORDER BY createdAt DESC LIMIT 3")
    fun getLastThree(customerId: String): LiveData<List<CustomerReading>>

    @Query("SELECT * FROM customer_readings WHERE id=:customerId ORDER BY createdAt DESC LIMIT 1")
    fun getIfHasRecord(customerId: String): LiveData<CustomerReading>
}