package com.example.bl_app.objects

import kotlinx.serialization.Serializable

@Serializable
data class MatchResult(
    val pointsTeam1: Int,
    val pointsTeam2: Int,
    val resultDescription: String?,
    val resultID: Int,
    val resultName: String,
    val resultOrderID: Int,
    val resultTypeID: Int,
)
