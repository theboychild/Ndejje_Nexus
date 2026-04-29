package ug.ac.ndejje.nexus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ug.ac.ndejje.nexus.repository.Reservation
import ug.ac.ndejje.nexus.repository.ReservationStatus
import ug.ac.ndejje.nexus.viewmodel.ShuttleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransportScreen(
    viewModel: ShuttleViewModel,
    onNavigateBack: () -> Unit,
) {
    val reservations by viewModel.reservations.collectAsState()
    val schedules by viewModel.schedules.collectAsState()
    var showAddScheduleDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Driver Dashboard") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddScheduleDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Schedule")
            }
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
                    "Schedules",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            items(schedules) { schedule ->
                ScheduleItem(schedule)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Student Reservations",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            items(reservations) { reservation ->
                ReservationItem(
                    reservation = reservation,
                    onAccept = { viewModel.updateReservationStatus(reservation.id, ReservationStatus.GRANTED) },
                    onDeny = { viewModel.updateReservationStatus(reservation.id, ReservationStatus.DENIED) }
                )
            }
        }
    }

    if (showAddScheduleDialog) {
        AddScheduleDialog(
            onDismiss = { showAddScheduleDialog = false },
            onConfirm = { route, time ->
                viewModel.addSchedule(route, time)
                showAddScheduleDialog = false
            }
        )
    }
}

@Composable
fun ScheduleItem(schedule: Pair<String, String>) {
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
        }
    }
}

@Composable
fun ReservationItem(
    reservation: Reservation,
    onAccept: () -> Unit,
    onDeny: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(reservation.studentName, fontWeight = FontWeight.Bold)
            Text(reservation.route, style = MaterialTheme.typography.bodyMedium)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (reservation.status == ReservationStatus.PENDING) {
                    IconButton(onClick = onDeny) {
                        Icon(Icons.Default.Close, contentDescription = "Deny", tint = Color.Red)
                    }
                    IconButton(onClick = onAccept) {
                        Icon(Icons.Default.Check, contentDescription = "Accept", tint = Color.Green)
                    }
                } else {
                    val statusColor = when(reservation.status) {
                        ReservationStatus.GRANTED -> Color.Green
                        ReservationStatus.DENIED -> Color.Red
                        else -> Color.Gray
                    }
                    Text(
                        reservation.status.name,
                        color = statusColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var route by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Schedule") },
        text = {
            Column {
                OutlinedTextField(
                    value = route,
                    onValueChange = { route = it },
                    label = { Text("Route (e.g. Main to Kampala)") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Time (e.g. 10:00 AM)") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(route, time) }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
