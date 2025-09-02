package com.superapps.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.superapps.data.database.model.BreedEntity

@Dao
interface BreedsDao {
    @Query("SELECT * FROM ${BreedEntity.TABLE_NAME}")
    suspend fun getAll(): List<BreedEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(breeds: List<BreedEntity>)
}
