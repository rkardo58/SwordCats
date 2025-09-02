package com.superapps.data.repository

import com.superapps.common.Resource
import com.superapps.common.ui.components.UiText
import com.superapps.data.database.dao.BreedsDao
import com.superapps.data.network.CatsApi
import com.superapps.domain.model.Breed
import com.superapps.domain.repository.BreedsRepository
import javax.inject.Inject

class BreedsRepositoryImpl
    @Inject
    constructor(
        private val api: CatsApi,
        private val breedDao: BreedsDao,
    ) : BreedsRepository {
        override suspend fun getBreedsPaged(
            page: Int,
            pageSize: Int,
        ): Resource<List<Breed>> =
            try {
                val remote = api.getBreeds(page, pageSize)
                val entities = remote.map { it.toEntity() }
                breedDao.insertAll(entities)
                Resource.Success(entities.map { it.toBreed() })
            } catch (e: Exception) {
                val cached = breedDao.getAll().map { it.toBreed() }
                if (cached.isNotEmpty()) {
                    Resource.Success(cached)
                } else {
                    Resource.Failed(UiText.DynamicString(e.localizedMessage ?: "Unknown error"))
                }
            }

        override suspend fun searchBreeds(query: String): Resource<List<Breed>> {
            TODO("Not yet implemented")
        }

        override suspend fun getBreedDetail(id: String): Resource<Breed> {
            TODO("Not yet implemented")
        }
    }
