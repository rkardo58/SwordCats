package com.superapps.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Breed(
	val id: String,
	val name: String,
	val description: String,
	val origin: String,
	val temperament: List<String>,
	val lifeSpan: String,
	val imageUrl: String,
	val isFavourite: Boolean
)
