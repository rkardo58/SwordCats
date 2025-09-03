package com.superapps.domain.usecase

import com.superapps.common.ui.components.State
import com.superapps.domain.model.Breed
import com.superapps.domain.repository.BreedsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchBreed @Inject constructor(private val repository: BreedsRepository) {
	operator fun invoke(query: String): Flow<State<List<Breed>>> = flow {
		emit(State.Loading())
		val result = repository.searchBreeds(query)
		emit(State.fromResource(result))
	}
}
