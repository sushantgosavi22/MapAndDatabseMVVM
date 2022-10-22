package com.sushant.mapanddatabasemvvm.dependancy

import android.content.Context
import com.sushant.mapanddatabasemvvm.database.MapRoomDatabase
import com.sushant.mapanddatabasemvvm.database.dao.LocationEntryDao
import com.sushant.mapanddatabasemvvm.database.repository.LocationEntryRepo

class DependencyExtensions {
    //We can use dependency injection mechanism  here as well
    private fun provideLocationEntryDao(context: Context) : LocationEntryDao =
        MapRoomDatabase.getDatabase(context).locationEntryDao()
    fun provideLocationEntryRepository(context: Context) =
        LocationEntryRepo(provideLocationEntryDao(context))
}