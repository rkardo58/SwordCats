package com.superapps.cats.breeds.model

import androidx.paging.PagingData
import com.superapps.common.ui.components.UiText
import com.superapps.domain.model.Breed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal data class BreedsState(
	val isLoading: Boolean = false,
	val searchQuery: String = "",
	val breeds: Flow<PagingData<Breed>> = emptyFlow(),
	val error: UiText? = null
)
