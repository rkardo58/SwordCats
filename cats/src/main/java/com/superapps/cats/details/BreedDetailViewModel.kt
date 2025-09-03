package com.superapps.cats.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.superapps.common.ui.components.State
import com.superapps.domain.model.Breed
import com.superapps.domain.usecase.FavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class BreedDetailViewModel @Inject constructor(private val favouriteUseCase: FavouriteUseCase) : ViewModel() {

	private val _state = MutableStateFlow(
		Breed("", "", "", "", emptyList(), "", "", false)
	)
	val state: StateFlow<Breed> = _state

	fun setBreed(breed: Breed) {
		_state.value = breed
	}

	fun onFavouriteClick(isFavourite: Boolean) {
		viewModelScope.launch {
			favouriteUseCase(state.value.id, isFavourite).collect { breed ->
				if (breed.isSuccessful()) {
					_state.value = (breed as State.Success<Breed>).data
				}
			}
		}
	}
}
