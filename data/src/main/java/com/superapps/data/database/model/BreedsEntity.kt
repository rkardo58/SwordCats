package com.superapps.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.superapps.data.database.model.BreedEntity.Companion.TABLE_NAME
import com.superapps.domain.model.Breed
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = TABLE_NAME)
data class BreedEntity(
    @PrimaryKey() val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
) {
    companion object {
        const val TABLE_NAME = "breeds_table"
    }

    fun toBreed() =
        Breed(
            id = id,
            name = name,
            description = description,
            imageUrl = imageUrl,
        )
}
