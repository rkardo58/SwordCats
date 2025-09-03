package com.superapps.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.superapps.data.database.model.BreedEntity

@Dao
interface BreedsDao {

	@Query("SELECT * FROM ${BreedEntity.TABLE_NAME} ORDER BY name ASC")
	fun getPagedBreeds(): PagingSource<Int, BreedEntity>

	@Query("SELECT * FROM ${BreedEntity.TABLE_NAME}")
	suspend fun getAll(): List<BreedEntity>

	@Query("SELECT * FROM ${BreedEntity.TABLE_NAME} WHERE id = :id")
	suspend fun getById(id: String): BreedEntity?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertAll(breeds: List<BreedEntity>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(breed: BreedEntity)

	@Query("SELECT * FROM ${BreedEntity.TABLE_NAME} WHERE isFavourite = 1 ORDER BY name ASC")
	suspend fun getAllFavourite(): List<BreedEntity>

	@Query("DELETE FROM ${BreedEntity.TABLE_NAME}")
	fun clearAll()
}
