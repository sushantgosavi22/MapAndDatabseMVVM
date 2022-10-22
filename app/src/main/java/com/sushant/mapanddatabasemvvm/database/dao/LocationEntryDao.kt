package com.sushant.mapanddatabasemvvm.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sushant.mapanddatabasemvvm.database.model.LocationEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationEntryDao {

    @Query("SELECT * FROM LocationEntry ORDER BY locationId")
    fun getLocationEntry(): Flow<MutableList<LocationEntry>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insert(locationEntry: LocationEntry)
}