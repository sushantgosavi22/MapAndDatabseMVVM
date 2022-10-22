package com.sushant.mapanddatabasemvvm.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sushant.mapanddatabasemvvm.database.dao.LocationEntryDao
import com.sushant.mapanddatabasemvvm.database.model.LocationEntry

@Database(entities = [LocationEntry::class], version = 1, exportSchema = false)
abstract class MapRoomDatabase : RoomDatabase() {
    abstract fun locationEntryDao(): LocationEntryDao

    companion object {
        @Volatile
        private var INSTANCE: MapRoomDatabase? = null

        fun getDatabase(context: Context): MapRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MapRoomDatabase::class.java,
                    "map_location_entry_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}