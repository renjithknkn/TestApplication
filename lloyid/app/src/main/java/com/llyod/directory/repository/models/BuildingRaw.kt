package com.llyod.directory.repository.models

data class BuildingRaw(
    val createdAt : String,
    val isOccupied: Boolean,
    val maxOccupancy: Int,
    val id: String
)
