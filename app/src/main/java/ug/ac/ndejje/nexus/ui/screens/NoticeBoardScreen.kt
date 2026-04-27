package ug.ac.ndejje.nexus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticeBoardScreen(
    viewModel: DashboardViewModel,
    onNavigateBack: () -> Unit,
) {
    var selectedCategory by remember { mutableStateOf(NoticeCategory.ALL) }
    var showAddDialog by remember { mutableStateOf(false) }
    
    val dashboardState by viewModel.dashboardState.collectAsState()
    val state = dashboardState

    if (showAddDialog) {
        AddNoticeDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, content, category ->
                viewModel.addNotice(title, content, category)
                showAddDialog = false
            }
        )
    }

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
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Notice")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            
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

            when (state) {
                is DashboardUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is DashboardUiState.Success -> {
                    val allNotices = state.notices
                    val filteredNotices = if (selectedCategory == NoticeCategory.ALL) {
                        allNotices
                    } else {
                        allNotices.filter { it.category == selectedCategory }
                    }

                    if (filteredNotices.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No notices found in this category.")
                        }
                    } else {
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
                is DashboardUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                    }
                }
                DashboardUiState.Idle -> {}
            }
        }
    }
}

@Composable
fun AddNoticeDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, NoticeCategory) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(NoticeCategory.ACADEMIC) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Notice") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Content") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                Text("Category", style = MaterialTheme.typography.labelLarge)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NoticeCategory.entries.filter { it != NoticeCategory.ALL }.forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat.name) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(title, content, category) },
                enabled = title.isNotBlank() && content.isNotBlank()
            ) {
                Text("Post")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
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
