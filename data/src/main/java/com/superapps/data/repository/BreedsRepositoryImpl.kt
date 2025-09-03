package com.superapps.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.superapps.common.R
import com.superapps.common.Resource
import com.superapps.common.ui.components.UiText
import com.superapps.data.R as dataRes
import com.superapps.data.database.SwordCatsDataBase
import com.superapps.data.database.dao.BreedsDao
import com.superapps.data.network.CatsApi
import com.superapps.domain.model.Breed
import com.superapps.domain.repository.BreedsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class BreedsRepositoryImpl @Inject constructor(private val api: CatsApi, private val dataBase: SwordCatsDataBase) : BreedsRepository {

	private val breedDao: BreedsDao = dataBase.breedsDao()

	@OptIn(ExperimentalPagingApi::class)
	override fun getBreedsPaged(): Flow<PagingData<Breed>> = Pager(
		config = PagingConfig(
			pageSize = 20,
			prefetchDistance = 30
		),
		remoteMediator = BreedsRemoteMediator(api, dataBase),
		pagingSourceFactory = { breedDao.getPagedBreeds() }
	).flow.map { pagingData ->
		pagingData.map { entity -> entity.toBreed() }
	}

	override suspend fun searchBreeds(query: String): Resource<List<Breed>> = try {
		val remote = api.searchBreeds(query)
		val localFavs = breedDao.getAllFavourite()
		val entities = remote.map { remote ->
			remote.toEntity(localFavs.any { it.id == remote.id })
		}
		breedDao.insertAll(entities)
		Resource.Success(entities.map { it.toBreed() })
	} catch (e: Exception) {
		val cached = breedDao.getAll().mapNotNull {
			if (it.name.lowercase().contains(query.lowercase())) {
				it.toBreed()
			} else {
				null
			}
		}
		if (cached.isNotEmpty()) {
			Resource.Success(cached)
		} else {
			getErrorMessage(e)
		}
	}

	private fun <T> getErrorMessage(e: Exception): Resource.Failed<T> = Resource.Failed(
		e.localizedMessage?.let {
			UiText.DynamicString(it)
		} ?: UiText.StringResource(R.string.error_message)
	)

	override suspend fun getAllFavourite(): Resource<List<Breed>> = try {
		Resource.Success(breedDao.getAllFavourite().map { it.toBreed() })
	} catch (e: Exception) {
		Timber.e(e)
		getErrorMessage(e)
	}

	override suspend fun toggleFavourite(id: String, isFavourite: Boolean): Resource<Breed> {
		try {
			val breed = breedDao.getById(id)
			return if (breed != null) {
				val newBreed = breed.copy(isFavourite = isFavourite)
				breedDao.insert(newBreed)
				Resource.Success(newBreed.toBreed())
			} else {
				Timber.e(Exception("No breed found with id $id"))
				Resource.Failed(UiText.StringResource(dataRes.string.no_breeds_found))
			}
		} catch (e: Exception) {
			Timber.e(e)
			return getErrorMessage(e)
		}
	}
}
