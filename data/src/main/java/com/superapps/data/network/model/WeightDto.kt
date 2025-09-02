package com.superapps.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class WeightDto(
    val imperial: String,
    val metric: String,
)
