package ug.ac.ndejje.nexus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ug.ac.ndejje.nexus.model.User
import ug.ac.ndejje.nexus.repository.ReservationStatus
import ug.ac.ndejje.nexus.viewmodel.ShuttleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeatReservationScreen(
    user: User,
    viewModel: ShuttleViewModel,
    onNavigateBack: () -> Unit
) {
    val reservations by viewModel.reservations.collectAsState()
    val schedules by viewModel.schedules.collectAsState()
    
    // Filter reservations for the current student
    val myReservations = reservations.filter { it.studentName == user.name }

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "New Reservation",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            if (schedules.isEmpty()) {
                item { Text("No active routes for reservation.") }
            } else {
                items(schedules) { schedule ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(schedule.first, fontWeight = FontWeight.Bold)
                                Text(schedule.second, style = MaterialTheme.typography.bodyMedium)
                            }
                            Button(onClick = { viewModel.makeReservation(user.name, schedule.first) }) {
                                Text("Reserve")
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "My Reservations",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            if (myReservations.isEmpty()) {
                item { Text("You have no reservations.") }
            } else {
                items(myReservations) { reservation ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(reservation.route, fontWeight = FontWeight.Bold)
                                val statusText = when(reservation.status) {
                                    ReservationStatus.PENDING -> "Pending"
                                    ReservationStatus.GRANTED -> "Granted"
                                    ReservationStatus.DENIED -> "Denied"
                                }
                                val statusColor = when(reservation.status) {
                                    ReservationStatus.PENDING -> Color.Gray
                                    ReservationStatus.GRANTED -> Color.Green
                                    ReservationStatus.DENIED -> Color.Red
                                }
                                Text(
                                    statusText,
                                    color = statusColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
