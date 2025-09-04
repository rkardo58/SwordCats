package com.superapps.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.superapps.data.database.SwordCatsDataBase
import com.superapps.data.database.model.BreedEntity
import com.superapps.data.network.CatsApi
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class BreedsRemoteMediator @Inject constructor(private val api: CatsApi, private val dataBase: SwordCatsDataBase) :
	RemoteMediator<Int, BreedEntity>() {

	private var currentPage = 0

	override suspend fun load(loadType: LoadType, state: PagingState<Int, BreedEntity>): MediatorResult {
		val breedDao = dataBase.breedsDao()
		return try {
			val page = when (loadType) {
				LoadType.REFRESH -> 0
				LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
				LoadType.APPEND -> currentPage + 1
			}

			currentPage = page

			val remote = api.getBreeds(page, state.config.pageSize)
			val localFavs = breedDao.getAllFavourite().first().map { it.id }.toSet()

			val entities = remote.map { remoteBreed ->
				remoteBreed.toEntity(remoteBreed.id in localFavs)
			}

			dataBase.withTransaction {
				if (loadType == LoadType.REFRESH) {
					breedDao.clearAll()
				}

				breedDao.insertAll(entities)
			}

			val endOfPaginationReached = remote.size < state.config.pageSize
			MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
		} catch (e: Exception) {
			Timber.e(e)
			MediatorResult.Error(e)
		}
	}
}
