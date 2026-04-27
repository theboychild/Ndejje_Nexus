package ug.ac.ndejje.nexus.repository

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay

data class ShuttleInfo(
    val position: LatLng,
    val eta: Int
)

class ShuttleRepository {
    /**
     * Fetches live shuttle info from the server.
     * Restored coordinates for Ndejje University campuses to ensure map loads correctly.
     */
    suspend fun getLiveShuttleInfo(): Result<ShuttleInfo?> {
        delay(1000)
        // Providing a real location (Main Campus Luwero) so the map has something to show
        return Result.success(
            ShuttleInfo(
                position = LatLng(0.8354, 32.5055), 
                eta = 15
            )
        ) 
    }

    /**
     * Fetches the static shuttle schedules.
     */
    suspend fun getSchedules(): Result<List<Pair<String, String>>> {
        delay(500)
        return Result.success(listOf(
            "Main Campus to Kampala" to "07:00 AM",
            "Kampala to Main Campus" to "08:30 AM"
        ))
    }
}
