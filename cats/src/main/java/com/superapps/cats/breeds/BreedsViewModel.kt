package com.superapps.cats.breeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.superapps.cats.breeds.model.BreedsState
import com.superapps.common.ui.components.State
import com.superapps.domain.usecase.FavouriteUseCase
import com.superapps.domain.usecase.GetBreedsPaged
import com.superapps.domain.usecase.SearchBreed
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
internal class BreedsViewModel @Inject constructor(
	getBreedsPaged: GetBreedsPaged,
	private val favouriteUseCase: FavouriteUseCase,
	private val searchBreed: SearchBreed
) : ViewModel() {

	private val _state = MutableStateFlow(BreedsState())
	val state: StateFlow<BreedsState> = _state
	private val breeds = getBreedsPaged().onEach {
		Timber.tag("artur2").d("-----------------------------------------------------")
	}.cachedIn(viewModelScope)

	init {
		_state.update {
			it.copy(breeds = breeds)
		}
	}

	fun onSearchQueryChange(query: String) {
		_state.update {
			it.copy(searchQuery = query)
		}

		if (query.isEmpty()) {
			_state.update {
				it.copy(breeds = breeds)
			}
		} else {
			viewModelScope.launch {
				searchBreed(query).debounce(500).collect { state ->
					_state.update {
						when (state) {
							is State.Error -> it.copy(error = state.message, isLoading = false)
							is State.Loading -> it.copy(isLoading = true)
							is State.Success -> {
								it.copy(
									breeds = flowOf(PagingData.from(state.data)),
									isLoading = false
								)
							}
						}
					}
				}
			}
		}
	}

	fun onToggleFavorite(id: String, isFavourite: Boolean) {
		viewModelScope.launch {
			favouriteUseCase(id, isFavourite).collect { resultState ->
				if (resultState is State.Success && state.value.searchQuery.isNotEmpty()) {
					val currentBreeds = state.value.breeds.firstOrNull()
					val updated = currentBreeds?.map { breed ->
						if (breed.id == id) {
							breed.copy(isFavourite = !breed.isFavourite)
						} else {
							breed
						}
					} ?: return@collect

					_state.value = _state.value.copy(breeds = flowOf(updated))
				}
			}
		}
	}
}
