package com.sushant.mapanddatabasemvvm.database.repository

import androidx.annotation.WorkerThread
import com.sushant.mapanddatabasemvvm.database.dao.LocationEntryDao
import com.sushant.mapanddatabasemvvm.database.model.LocationEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class LocationEntryRepo(private val locationEntryDao: LocationEntryDao) : LocationEnterRepository {

    override suspend fun getLocationEntry(): Flow<MutableList<LocationEntry>> =  locationEntryDao.getLocationEntry()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun insert(locationEntry: LocationEntry) : Flow<Boolean>  = flow {
        locationEntryDao.insert(locationEntry)
        emit(true)
    }.catch { emit(false) }

}