package com.sushant.mapanddatabasemvvm.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LocationEntry")
data class LocationEntry(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "locationId")
    var locationId: Int? = null,

    @ColumnInfo(name = "propertyName")
    val propertyName: String,

    @ColumnInfo(name = "coordinates")
    val coordinates: String
)