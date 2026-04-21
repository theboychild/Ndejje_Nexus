package ug.ac.ndejje.nexus.model

data class Shuttle(
    val id: String = "",
    val route: String = "",
    val eta: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val status: String = "Normal"
)