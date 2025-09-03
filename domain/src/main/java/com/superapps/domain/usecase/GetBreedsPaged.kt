package com.superapps.domain.usecase

import androidx.paging.PagingData
import com.superapps.domain.model.Breed
import com.superapps.domain.repository.BreedsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetBreedsPaged @Inject constructor(private val repository: BreedsRepository) {
	operator fun invoke(): Flow<PagingData<Breed>> = repository.getBreedsPaged()
}
