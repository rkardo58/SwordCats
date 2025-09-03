package com.superapps.domain.usecase

import com.superapps.common.ui.components.State
import com.superapps.domain.model.Breed
import com.superapps.domain.repository.BreedsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteUseCase @Inject constructor(private val repository: BreedsRepository) {
	operator fun invoke(id: String, isFavourite: Boolean): Flow<State<Breed>> = flow {
		emit(State.Loading())
		val result = repository.toggleFavourite(id, isFavourite)
		emit(State.fromResource(result))
	}
}
