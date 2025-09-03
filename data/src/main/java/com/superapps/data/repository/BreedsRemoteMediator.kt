package com.superapps.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.superapps.data.database.SwordCatsDataBase
import com.superapps.data.database.model.BreedEntity
import com.superapps.data.network.CatsApi
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class BreedsRemoteMediator(private val api: CatsApi, private val dataBase: SwordCatsDataBase) : RemoteMediator<Int, BreedEntity>() {

	override suspend fun load(loadType: LoadType, state: PagingState<Int, BreedEntity>): MediatorResult {
		val breedDao = dataBase.breedsDao()
		return try {
			val page = when (loadType) {
				LoadType.REFRESH -> 0
				LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
				LoadType.APPEND -> {
					val loadedCount = state.pages.sumOf { it.data.size }
					loadedCount / state.config.pageSize
				}
			}
			val remote = api.getBreeds(page, state.config.pageSize)
			val localFavs = breedDao.getAllFavourite()

			val entities = remote.map { remoteBreed ->
				Timber.tag("artur").d("Remote breed: ${remoteBreed.name}")
				remoteBreed.toEntity(localFavs.any { it.id == remoteBreed.id })
			}

			dataBase.withTransaction {
				if (loadType == LoadType.REFRESH) {
					breedDao.clearAll()
				}

				breedDao.insertAll(entities)
			}
			Timber.tag("artur").d("MediatorResult.Success ${remote.size} $page")
			MediatorResult.Success(endOfPaginationReached = remote.size < state.config.pageSize)
		} catch (e: Exception) {
			Timber.e(e)
			MediatorResult.Error(e)
		}
	}
}
