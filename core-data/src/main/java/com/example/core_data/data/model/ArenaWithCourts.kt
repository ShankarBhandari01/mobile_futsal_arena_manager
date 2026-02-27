package com.example.core_data.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class ArenaWithCourts(
    @Embedded val arena: Arenas?,
    @Relation(
        parentColumn = "id",
        entityColumn = "arenaId"
    )
    val courts: List<Courts?>
)