package com.superapps.cats.utils

import com.superapps.domain.model.Breed

fun createBreed(
	id: String = "1",
	name: String = "Persian",
	lifespan: String = "12-15",
	isFavourite: Boolean = false
) = Breed(
	id = id,
	name = name,
	description = "Fluffy cat",
	imageUrl = "https://example.com/cat.jpg",
	origin = "Iran",
	temperament = listOf("Calm"),
	lifespan = lifespan,
	isFavourite = isFavourite
)
