package com.superapps.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageDto(val height: Int, val id: String, val url: String, val width: Int)
