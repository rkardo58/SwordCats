package com.superapps.data.network.model

import com.superapps.data.database.model.BreedEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BreedDto(
	val adaptability: Int,
	@SerialName("affection_level")
	val affectionLevel: Int,
	@SerialName("alt_names")
	val altNames: String? = "",
	@SerialName("cfa_url")
	val cfaUrl: String? = null,
	@SerialName("child_friendly")
	val childFriendly: Int,
	@SerialName("country_code")
	val countryCode: String,
	@SerialName("country_codes")
	val countryCodes: String,
	val description: String,
	@SerialName("dog_friendly")
	val dogFriendly: Int,
	@SerialName("energy_level")
	val energyLevel: Int,
	val experimental: Int,
	val grooming: Int,
	val hairless: Int,
	@SerialName("health_issues")
	val healthIssues: Int,
	val hypoallergenic: Int,
	val id: String,
	val image: ImageDto? = null,
	val indoor: Int,
	val intelligence: Int,
	val lap: Int? = null,
	@SerialName("life_span")
	val lifeSpan: String,
	val name: String,
	val natural: Int,
	val origin: String,
	val rare: Int,
	@SerialName("reference_image_id")
	val referenceImageId: String? = null,
	val rex: Int,
	@SerialName("shedding_level")
	val sheddingLevel: Int,
	@SerialName("short_legs")
	val shortLegs: Int,
	@SerialName("social_needs")
	val socialNeeds: Int,
	@SerialName("stranger_friendly")
	val strangerFriendly: Int,
	@SerialName("suppressed_tail")
	val suppressedTail: Int,
	val temperament: String,
	@SerialName("vcahospitals_url")
	val hospitalsUrl: String? = null,
	@SerialName("vetstreet_url")
	val vetStreetUrl: String? = null,
	val vocalisation: Int,
	val weight: WeightDto,
	@SerialName("wikipedia_url")
	val wikipediaUrl: String? = ""
) {
	fun toEntity(isFavourite: Boolean) = BreedEntity(
		name = name,
		description = description,
		id = id,
		imageUrl = image?.url ?: "",
		isFavourite = isFavourite,
		lifeSpan = lifeSpan,
		origin = origin,
		temperament = temperament
	)
}
