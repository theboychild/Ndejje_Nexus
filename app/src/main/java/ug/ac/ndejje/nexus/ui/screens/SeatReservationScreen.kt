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
    var selectedRoute by remember { mutableStateOf("Main to Kampala - 07:00 AM") }
    val routes = listOf(
        "Main to Kampala - 07:00 AM",
        "Kampala to Main - 08:30 AM",
        "Lady Irene to Main - 09:00 AM"
    )

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Reserve Seat")
        }
    }
}
