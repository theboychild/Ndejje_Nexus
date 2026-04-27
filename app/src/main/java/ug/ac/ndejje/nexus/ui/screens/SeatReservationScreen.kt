package ug.ac.ndejje.nexus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeatReservationScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seat Reservation") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        SeatReservationContent(modifier = Modifier.padding(padding))
    }
}

@Composable
fun SeatReservationContent(modifier: Modifier = Modifier) {
    // Dropped demo routes
    val routes by remember { mutableStateOf(emptyList<String>()) }
    var selectedRoute by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (routes.isEmpty()) {
            Text("No active routes for reservation.")
        } else {
            Text(text = "Select Route", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            
            routes.forEach { route ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = selectedRoute == route, onClick = { selectedRoute = route })
                    Text(text = route, style = MaterialTheme.typography.bodyLarge)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { /* TODO: Submit reservation to API */ },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Reserve Seat")
            }
        }
    }
}
