package ug.ac.ndejje.nexus.repository

import kotlinx.coroutines.delay
import ug.ac.ndejje.nexus.model.Notice
import ug.ac.ndejje.nexus.model.NoticeCategory
import java.text.SimpleDateFormat
import java.util.*

/**
 * Singleton repository for Notice Board.
 */
object NoticeRepository {
    private val _notices = mutableListOf<Notice>()

    suspend fun getNotices(): Result<List<Notice>> {
        delay(500)
        return Result.success(_notices.toList()) 
    }

    suspend fun addNotice(title: String, content: String, category: NoticeCategory): Result<Unit> {
        delay(500)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = sdf.format(Date())
        
        val newNotice = Notice(
            id = UUID.randomUUID().toString(),
            title = title,
            content = content,
            date = currentDate,
            category = category
        )
        _notices.add(0, newNotice)
        return Result.success(Unit)
    }
}
