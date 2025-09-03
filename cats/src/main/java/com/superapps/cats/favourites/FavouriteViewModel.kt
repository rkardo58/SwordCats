package com.superapps.cats.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.superapps.cats.favourites.model.FavouriteState
import com.superapps.common.ui.components.State
import com.superapps.domain.model.Breed
import com.superapps.domain.usecase.FavouriteUseCase
import com.superapps.domain.usecase.GetAllFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class FavouriteViewModel @Inject constructor(
	private val getAllFavouriteUseCase: GetAllFavouriteUseCase,
	private val favouriteUseCase: FavouriteUseCase
) : ViewModel() {

	private val _state = MutableStateFlow(FavouriteState())
	val state: StateFlow<FavouriteState> = _state

	init {
		getFavourites()
	}

	fun getFavourites() {
		viewModelScope.launch {
			getAllFavouriteUseCase().collect { resultState ->
				_state.update {
					when (resultState) {
						is State.Error -> it.copy(isLoading = false)
						is State.Loading -> it.copy(isLoading = true)
						is State.Success -> {
							it.copy(breeds = resultState.data, isLoading = false, averageLifeSpan = getLifeSpan(resultState.data))
						}
					}
				}
			}
		}
	}

	private fun getLifeSpan(breeds: List<Breed>): Int {
		var totalLifeSpan = 0
		var lifeSpanCount = 0
		breeds.forEach { breed ->
			val catLifeSpan = breed.lifeSpan.split("-").lastOrNull()?.trim()?.toIntOrNull() ?: 0
			if (catLifeSpan > 0) {
				totalLifeSpan += catLifeSpan
				lifeSpanCount++
			}
		}
		return totalLifeSpan / lifeSpanCount
	}

	fun removeFromFav(id: String) {
		viewModelScope.launch {
			favouriteUseCase(id, false).collect { state ->
				if (state is State.Success) {
					_state.update {
						val filteredBreeds = it.breeds.filter { it.id != id }
						it.copy(breeds = it.breeds.filter { it.id != id }, isLoading = false, averageLifeSpan = getLifeSpan(filteredBreeds))
					}
				}
			}
		}
	}
}
