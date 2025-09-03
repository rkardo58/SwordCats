package com.superapps.data.network

import com.superapps.data.network.model.BreedDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CatsApi {
	@GET("breeds")
	suspend fun getBreeds(@Query("page") page: Int, @Query("limit") limit: Int): List<BreedDto>

	@GET("breeds/search")
	suspend fun searchBreeds(@Query("q") query: String, @Query("attach_image") attachImage: Int = 1): List<BreedDto>
}
