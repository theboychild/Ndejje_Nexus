package ug.ac.ndejje.nexus.repository

import kotlinx.coroutines.delay
import ug.ac.ndejje.nexus.model.Notice
import ug.ac.ndejje.nexus.model.NoticeCategory

class NoticeRepository {
    /**
     * Fetches the latest notices from the server.
     * Currently returns an empty list, ready for API integration.
     */
    suspend fun getNotices(): Result<List<Notice>> {
        delay(1000)
        // TODO: Replace with real API call
        // return Result.success(fetchFromApi())
        return Result.success(emptyList()) 
    }
}
