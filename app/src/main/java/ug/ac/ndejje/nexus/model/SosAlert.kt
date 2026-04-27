package ug.ac.ndejje.nexus.model

import java.util.*

enum class SosStatus {
    ACTIVE, RESPONDING, RESOLVED, CANCELLED
}

data class SosAlert(
    val id: String = UUID.randomUUID().toString(),
    val studentName: String,
    val studentRegNumber: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: SosStatus = SosStatus.ACTIVE,
    val location: String = "Main Campus" // Placeholder for real GPS coords
)
