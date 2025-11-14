package com.example.crikstats.data

data class PlayerResponse(
    val data: List<Player>
)
data class Player(
    val resource: String,
    val id: Int,
    val country_id: Int,
    val firstname: String,
    val lastname: String,
    val fullname: String,
    val image_path: String,
    val dateofbirth: String,
    val gender: String,
    val battingstyle: String?,
    val bowlingstyle: String?,
    val position: Position,
    val updated_at: String
)
data class Position(
    val resource: String,
    val id: Int,
    val name: String
)
