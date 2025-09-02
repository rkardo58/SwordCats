package com.superapps.data.network

import com.superapps.data.network.model.BreedDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CatsApi {
    @GET("breeds")
    suspend fun getBreeds(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): List<BreedDto>
}
