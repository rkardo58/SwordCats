package com.superapps.domain.repository

import com.superapps.common.Resource
import com.superapps.domain.model.Breed

interface BreedsRepository {
    suspend fun getBreedsPaged(
        page: Int,
        pageSize: Int,
    ): Resource<List<Breed>>

    suspend fun searchBreeds(query: String): Resource<List<Breed>>

    suspend fun getBreedDetail(id: String): Resource<Breed>
}
