package com.superapps.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.superapps.core.database.model.BreedEntity.Companion.TABLE_NAME
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = TABLE_NAME)
data class BreedEntity(
    @PrimaryKey() val id: String,
) {
    companion object {
        const val TABLE_NAME = "breeds_table"
    }
}