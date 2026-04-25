package ug.ac.ndejje.nexus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShuttleScheduleScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shuttle Schedules") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        ShuttleScheduleContent(modifier = Modifier.padding(padding))
    }
}

@Composable
fun ShuttleScheduleContent(modifier: Modifier = Modifier) {
    val schedules = listOf(
        "Main Campus to Kampala" to "07:00 AM",
        "Kampala to Main Campus" to "08:30 AM",
        "Lady Irene to Main Campus" to "09:00 AM",
        "Main Campus to Lady Irene" to "12:00 PM"
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(schedules) { (route, time) ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = route, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                    Text(text = time, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}
