package com.superapps.data.utils

import com.superapps.data.database.model.BreedEntity
import com.superapps.data.network.model.BreedDto
import com.superapps.data.network.model.ImageDto
import com.superapps.data.network.model.WeightDto
import com.superapps.domain.model.Breed

fun createBreedEntity(id: String = "1", name: String = "Persian", isFavourite: Boolean = false) = BreedEntity(
	id = id,
	name = name,
	description = "Fluffy cat",
	imageUrl = "https://example.com/cat.jpg",
	origin = "Iran",
	temperament = "Calm",
	lifespan = "12-15",
	isFavourite = isFavourite
)

fun createBreedDto(
	id: String = "1",
	name: String = "Siamese",
	description: String = "A very distinguished cat.",
	image: ImageDto? = ImageDto(height = 1080, id = "img1", url = "http://example.com/siamese.jpg", width = 1920),
	origin: String = "Thailand",
	temperament: String = "Active, Affectionate, Intelligent, Social, Vocal",
	lifeSpan: String = "12 - 15",
	adaptability: Int = 5,
	affectionLevel: Int = 5,
	childFriendly: Int = 4,
	countryCode: String = "TH",
	countryCodes: String = "TH",
	dogFriendly: Int = 3,
	energyLevel: Int = 5,
	experimental: Int = 0,
	grooming: Int = 2,
	hairless: Int = 0,
	healthIssues: Int = 3,
	hypoallergenic: Int = 0,
	indoor: Int = 1,
	intelligence: Int = 5,
	natural: Int = 0,
	rare: Int = 0,
	rex: Int = 0,
	sheddingLevel: Int = 3,
	shortLegs: Int = 0,
	socialNeeds: Int = 5,
	strangerFriendly: Int = 4,
	suppressedTail: Int = 0,
	vocalisation: Int = 5,
	weight: WeightDto = WeightDto(imperial = "8 - 12", metric = "4 - 5"),
	altNames: String? = "Meezer",
	cfaUrl: String? = "http://cfa.org/Breeds/BreedsSthruT/Siamese.aspx",
	referenceImageId: String? = "img1_ref",
	lap: Int? = 1,
	hospitalsUrl: String? = null,
	vetStreetUrl: String? = "http://www.vetstreet.com/cats/siamese",
	wikipediaUrl: String? = "https://en.wikipedia.org/wiki/Siamese_cat"
): BreedDto = BreedDto(
	adaptability = adaptability,
	affectionLevel = affectionLevel,
	altNames = altNames,
	cfaUrl = cfaUrl,
	childFriendly = childFriendly,
	countryCode = countryCode,
	countryCodes = countryCodes,
	description = description,
	dogFriendly = dogFriendly,
	energyLevel = energyLevel,
	experimental = experimental,
	grooming = grooming,
	hairless = hairless,
	healthIssues = healthIssues,
	hypoallergenic = hypoallergenic,
	id = id,
	image = image,
	indoor = indoor,
	intelligence = intelligence,
	lap = lap,
	lifeSpan = lifeSpan,
	name = name,
	natural = natural,
	origin = origin,
	rare = rare,
	referenceImageId = referenceImageId,
	rex = rex,
	sheddingLevel = sheddingLevel,
	shortLegs = shortLegs,
	socialNeeds = socialNeeds,
	strangerFriendly = strangerFriendly,
	suppressedTail = suppressedTail,
	temperament = temperament,
	hospitalsUrl = hospitalsUrl,
	vetStreetUrl = vetStreetUrl,
	vocalisation = vocalisation,
	weight = weight,
	wikipediaUrl = wikipediaUrl
)

fun createBreed(id: String = "1", name: String = "Persian", isFavourite: Boolean = false) = Breed(
	id = id,
	name = name,
	description = "Fluffy cat",
	imageUrl = "https://example.com/cat.jpg",
	origin = "Iran",
	temperament = listOf("Calm"),
	lifespan = "12-15",
	isFavourite = isFavourite
)

fun getBreedList() = listOf(
	createBreed(
		id = "1",
		name = "Persian",
		isFavourite = true
	),
	createBreed(
		id = "2",
		name = "Siamese",
		isFavourite = false
	),
	createBreed(
		id = "3",
		name = "Abyssinian",
		isFavourite = true
	),
	createBreed(
		id = "4",
		name = "Bengal",
		isFavourite = false
	)
)
