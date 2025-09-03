package com.superapps.domain.repository

import androidx.paging.PagingData
import com.superapps.common.Resource
import com.superapps.domain.model.Breed
import kotlinx.coroutines.flow.Flow

interface BreedsRepository {

	fun getBreedsPaged(): Flow<PagingData<Breed>>

	suspend fun searchBreeds(query: String): Resource<List<Breed>>

	suspend fun getAllFavourite(): Resource<List<Breed>>

	suspend fun toggleFavourite(id: String, isFavourite: Boolean): Resource<Breed>
}
