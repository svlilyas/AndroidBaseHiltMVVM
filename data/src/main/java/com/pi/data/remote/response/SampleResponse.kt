package com.pi.data.remote.response

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "sample_db")
data class SampleResponse(
    @ColumnInfo(name = "id")
    var id: String = "",
    @ColumnInfo(name = "name")
    var name: String = ""
)
