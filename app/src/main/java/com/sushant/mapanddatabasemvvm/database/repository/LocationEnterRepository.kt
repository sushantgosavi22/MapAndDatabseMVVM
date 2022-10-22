package com.sushant.mapanddatabasemvvm.database.repository

import com.sushant.mapanddatabasemvvm.database.model.LocationEntry
import kotlinx.coroutines.flow.Flow

interface LocationEnterRepository {
    suspend fun getLocationEntry(): Flow<MutableList<LocationEntry>>
    suspend fun insert(locationEntry: LocationEntry) : Flow<Boolean>
}