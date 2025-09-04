package com.superapps.domain.usecase

import com.superapps.common.ui.components.State
import com.superapps.domain.model.Breed
import com.superapps.domain.repository.BreedsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

class GetAllFavouriteUseCase @Inject constructor(private val repository: BreedsRepository) {
	operator fun invoke(): Flow<State<List<Breed>>> = repository.getAllFavourite().onStart {
		emit(State.loading())
	}
}
