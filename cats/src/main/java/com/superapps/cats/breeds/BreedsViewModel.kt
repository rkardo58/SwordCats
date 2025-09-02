package com.superapps.cats.breeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.superapps.cats.breeds.model.BreedsState
import com.superapps.common.ui.components.State
import com.superapps.domain.usecase.GetBreedsPaged
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class BreedsViewModel
    @Inject
    constructor(
        getBreedsPaged: GetBreedsPaged,
    ) : ViewModel() {
        private val _state = MutableStateFlow(BreedsState())
        val state: StateFlow<BreedsState> = _state

        init {
            val result = getBreedsPaged(0, 20)
            viewModelScope.launch {
                result.collect { state ->
                    _state.update {
                        when (state) {
                            is State.Error -> it.copy(error = state.message, isLoading = false)
                            is State.Loading -> it.copy(isLoading = true)
                            is State.Success -> it.copy(breeds = BreedsState.Breeds(state.data), isLoading = false)
                        }
                    }
                }
            }
        }
    }
