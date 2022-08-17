package com.pi.androidbasehiltmvvm.core.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_readings")
data class CustomerReading(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,
    val cost: Float? = 0f,
    val reading: Float? = 0f,
    val createdAt: Long?
) {

}
