/* 
 * This file contains the "Notice Board Screen." 
 * Think of this as the digital version of a wooden notice board at school where 
 * announcements are pinned up for students to see.
 *
 * HOW MVVM IS USED HERE:
 * 1. VIEW: NoticeBoardScreen.kt defines the filtered list and tab navigation.
 * 2. MODEL: The "Notice" data class is the "Model" representing an announcement.
 * 3. VIEW-MODEL: The "DashboardViewModel" provides the list of notices via "dashboardState."
 */
package ug.ac.ndejje.nexus.ui.screens

/* These are the "Tools" we use to build the visual parts of the screen. */
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import ug.ac.ndejje.nexus.viewmodel.DashboardUiState
import ug.ac.ndejje.nexus.viewmodel.DashboardViewModel

/**
 * NoticeBoardScreen is the main "page" for university-wide announcements.
 * 
 * @param viewModel This is the "Brain" of the screen. It fetches the notices and keeps track of updates.
 * @param onNavigateBack This is the "Action" that happens when a user clicks the back button.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticeBoardScreen(
    viewModel: DashboardViewModel,
    onNavigateBack: () -> Unit,
) {
    /* VIEW USAGE: Tracks the category selected by the student to filter the news. */
    var selectedCategory by remember { mutableStateOf(NoticeCategory.ALL) }
    
    /* VIEW-MODEL USAGE: We "Watch" the notice list from the DashboardViewModel. */
    val dashboardState by viewModel.dashboardState.collectAsState()
    val state = dashboardState

    /* VIEW USAGE: Main screen structure. */
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notice Board") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            
            /* FILTER TABS: Interactive menu for picking notice categories. */
            SecondaryScrollableTabRow(
                selectedTabIndex = selectedCategory.ordinal,
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                NoticeCategory.entries.forEach { category ->
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

            /* VIEW-MODEL USAGE: Decide what UI to show based on the current state from the ViewModel. */
            when (state) {
                /* 1. LOADING: Fetching data from the server. */
                is DashboardUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                
                /* 2. SUCCESS: Data arrived. We filter and display it. */
                is DashboardUiState.Success -> {
                    /* MODEL USAGE: We retrieve the list of "Notice" models from the Success state. */
                    val allNotices = state.notices
                    
                    /* VIEW LOGIC: Local filtering based on the selected tab. */
                    val filteredNotices = if (selectedCategory == NoticeCategory.ALL) {
                        allNotices
                    } else {
                        allNotices.filter { it.category == selectedCategory }
                    }

                    /* NOTICE LIST: Smooth-scrolling list of announcements. */
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
                
                /* 3. ERROR: Failed to fetch announcements. */
                is DashboardUiState.Error -> {
                    Text(text = state.message, modifier = Modifier.padding(16.dp))
                }
                
                DashboardUiState.Idle -> {}
            }
        }
    }
}

/**
 * NoticeCard is a visual "Sticky Note" for a single "Notice" Model.
 */
@Composable
fun NoticeCard(notice: Notice) {
    /* VIEW USAGE: Layout for a single notice entry. */
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
                /* MODEL USAGE: Accessing notice title and category. */
                Text(
                    text = notice.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                val containerColor = when(notice.category) {
                    NoticeCategory.ACADEMIC -> MaterialTheme.colorScheme.primary
                    NoticeCategory.SOCIAL -> MaterialTheme.colorScheme.secondary
                    NoticeCategory.FINANCIAL -> MaterialTheme.colorScheme.tertiary
                    NoticeCategory.ALL -> MaterialTheme.colorScheme.surfaceVariant
                }
                
                val contentColor = when(notice.category) {
                    NoticeCategory.ACADEMIC -> MaterialTheme.colorScheme.onPrimary
                    NoticeCategory.SOCIAL -> MaterialTheme.colorScheme.onSecondary
                    NoticeCategory.FINANCIAL -> MaterialTheme.colorScheme.onTertiary
                    NoticeCategory.ALL -> MaterialTheme.colorScheme.onSurfaceVariant
                }
                
                Badge(
                    containerColor = containerColor,
                    contentColor = contentColor
                ) {
                    Text(notice.category.name)
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            /* MODEL USAGE: Accessing notice content and date. */
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
