package com.superapps.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.superapps.core.database.dao.BreedsDao
import com.superapps.core.database.model.BreedEntity

@Database(entities = [BreedEntity::class], version = 1)
// @TypeConverters(Converters::class)
abstract class SwordCatsDataBase : RoomDatabase() {
    abstract fun breedsDao(): BreedsDao
}
