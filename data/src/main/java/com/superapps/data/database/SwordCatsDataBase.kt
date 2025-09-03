package com.superapps.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.superapps.data.database.dao.BreedsDao
import com.superapps.data.database.model.BreedEntity

@Database(entities = [BreedEntity::class], version = 1)
abstract class SwordCatsDataBase : RoomDatabase() {
	abstract fun breedsDao(): BreedsDao
}
