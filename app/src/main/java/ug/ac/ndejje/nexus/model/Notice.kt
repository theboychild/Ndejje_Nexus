package ug.ac.ndejje.nexus.model

enum class NoticeCategory {
    ALL, ACADEMIC, SOCIAL, FINANCIAL
}

data class Notice(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val date: String = "",
    val category: NoticeCategory = NoticeCategory.ACADEMIC
)