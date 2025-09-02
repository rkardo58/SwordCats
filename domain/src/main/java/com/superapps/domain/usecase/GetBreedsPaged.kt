package com.superapps.domain.usecase

import com.superapps.common.ui.components.State
import com.superapps.domain.model.Breed
import com.superapps.domain.repository.BreedsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetBreedsPaged
    @Inject
    constructor(
        private val repository: BreedsRepository,
    ) {
        operator fun invoke(
            page: Int,
            pageSize: Int,
        ): Flow<State<List<Breed>>> =
            flow {
                emit(State.Loading())
                val result = repository.getBreedsPaged(page, pageSize)
                emit(State.fromResource(result))
            }
    }
