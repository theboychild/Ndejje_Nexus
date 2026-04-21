package ug.ac.ndejje.nexus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ug.ac.ndejje.nexus.R
import ug.ac.ndejje.nexus.model.Notice
import ug.ac.ndejje.nexus.model.NoticeCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticeBoardScreen(onNavigateBack: () -> Unit) {
    var selectedCategory by remember { mutableStateOf(NoticeCategory.ALL) }
    
    val mockNotices = listOf(
        Notice("1", "Exam Timetable Out", "The end of semester exams will start on Dec 1st.", "2023-10-27", NoticeCategory.ACADEMIC),
        Notice("2", "Inter-Campus Games", "Join us this Friday at Main Campus for sports.", "2023-10-26", NoticeCategory.SOCIAL),
        Notice("3", "Tuition Deadline", "Final payment for semester 1 is due next week.", "2023-10-25", NoticeCategory.FINANCIAL),
        Notice("4", "Library Renovation", "The Lady Irene library will be closed this weekend.", "2023-10-24", NoticeCategory.ACADEMIC)
    )

    val filteredNotices = if (selectedCategory == NoticeCategory.ALL) {
        mockNotices
    } else {
        mockNotices.filter { it.category == selectedCategory }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notice Board") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            ScrollableTabRow(
                selectedTabIndex = selectedCategory.ordinal,
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                NoticeCategory.values().forEach { category ->
                    Tab(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        text = {
                            Text(
                                text = when (category) {
                                    NoticeCategory.ALL -> stringResource(R.string.notice_all)
                                    NoticeCategory.ACADEMIC -> stringResource(R.string.notice_academic)
                                    NoticeCategory.SOCIAL -> stringResource(R.string.notice_social)
                                    NoticeCategory.FINANCIAL -> stringResource(R.string.notice_financial)
                                }
                            )
                        }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredNotices) { notice ->
                    NoticeCard(notice)
                }
            }
        }
    }
}

@Composable
fun NoticeCard(notice: Notice) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notice.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Badge(containerColor = when(notice.category) {
                    NoticeCategory.ACADEMIC -> MaterialTheme.colorScheme.primary
                    NoticeCategory.SOCIAL -> MaterialTheme.colorScheme.secondary
                    NoticeCategory.FINANCIAL -> MaterialTheme.colorScheme.tertiary
                    NoticeCategory.ALL -> MaterialTheme.colorScheme.surfaceVariant
                }) {
                    Text(notice.category.name, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = notice.content, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = notice.date,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
