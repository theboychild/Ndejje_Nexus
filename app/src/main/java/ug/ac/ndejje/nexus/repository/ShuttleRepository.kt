package ug.ac.ndejje.nexus.repository

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

data class ShuttleInfo(
    val position: LatLng,
    val eta: Int
)

enum class ReservationStatus {
    PENDING, GRANTED, DENIED
}

data class Reservation(
    val id: String,
    val studentName: String,
    val route: String,
    val status: ReservationStatus = ReservationStatus.PENDING
)

/**
 * Singleton repository for Shuttle services.
 */
object ShuttleRepository {
    private val _reservations = MutableStateFlow<List<Reservation>>(emptyList())
    val reservations: StateFlow<List<Reservation>> = _reservations

    private val _schedules = MutableStateFlow<List<Pair<String, String>>>(listOf(
        "Main Campus to Kampala" to "07:00 AM",
        "Kampala to Main Campus" to "08:30 AM"
    ))
    val schedules: StateFlow<List<Pair<String, String>>> = _schedules

    suspend fun getLiveShuttleInfo(): Result<ShuttleInfo?> {
        delay(1000)
        return Result.success(
            ShuttleInfo(
                position = LatLng(0.8354, 32.5055), 
                eta = 15
            )
        ) 
    }

    suspend fun getSchedules(): Result<List<Pair<String, String>>> {
        delay(500)
        return Result.success(_schedules.value)
    }

    suspend fun addSchedule(route: String, time: String): Result<Unit> {
        delay(500)
        _schedules.value = _schedules.value + (route to time)
        return Result.success(Unit)
    }

    suspend fun makeReservation(studentName: String, route: String): Result<Unit> {
        delay(500)
        val newReservation = Reservation(
            id = UUID.randomUUID().toString(),
            studentName = studentName,
            route = route
        )
        _reservations.value = _reservations.value + newReservation
        return Result.success(Unit)
    }

    suspend fun updateReservationStatus(id: String, status: ReservationStatus): Result<Unit> {
        delay(500)
        _reservations.value = _reservations.value.map {
            if (it.id == id) it.copy(status = status) else it
        }
        return Result.success(Unit)
    }
}
