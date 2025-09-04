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
import kotlin.math.roundToInt
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
		observeFavourites()
	}

	private fun observeFavourites() {
		viewModelScope.launch {
			getAllFavouriteUseCase().collect { resultState ->
				_state.update {
					when (resultState) {
						is State.Error -> it.copy(isLoading = false, error = resultState.message)
						is State.Loading -> it.copy(isLoading = true, error = null)
						is State.Success -> {
							it.copy(
								breeds = resultState.data,
								isLoading = false,
								averageLifeSpan = getLifeSpan(resultState.data),
								error = null
							)
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
			val catLifeSpan = breed.lifespan.split("-").lastOrNull()?.trim()?.toIntOrNull() ?: 0
			if (catLifeSpan > 0) {
				totalLifeSpan += catLifeSpan
				lifeSpanCount++
			}
		}

		if (lifeSpanCount <= 0) return 0

		return (totalLifeSpan.toDouble() / lifeSpanCount).roundToInt()
	}

	fun removeFromFav(id: String) {
		viewModelScope.launch {
			favouriteUseCase(id, false).collect {
				if (it.isFailed()) {
					_state.update { state ->
						state.copy(error = (it as State.Error).message)
					}
				}
			}
		}
	}
}
