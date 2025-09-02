package com.superapps.cats.breeds.model

import androidx.compose.runtime.Stable
import com.superapps.common.ui.components.UiText
import com.superapps.domain.model.Breed

internal data class BreedsState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val breeds: Breeds = Breeds(),
    val error: UiText? = null,
) {
    @Stable
    data class Breeds(
        val list: List<Breed> = emptyList(),
    )
}
