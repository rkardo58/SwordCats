package com.superapps.cats.favourites.model

import com.superapps.domain.model.Breed

data class FavouriteState(val isLoading: Boolean = false, val breeds: List<Breed> = emptyList(), val averageLifeSpan: Int = 0)
