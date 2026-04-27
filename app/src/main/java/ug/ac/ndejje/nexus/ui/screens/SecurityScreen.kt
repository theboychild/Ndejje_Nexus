package ug.ac.ndejje.nexus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ug.ac.ndejje.nexus.model.SosStatus
import ug.ac.ndejje.nexus.viewmodel.SosViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityScreen(
    viewModel: SosViewModel,
    onNavigateBack: () -> Unit
) {
    val alerts by viewModel.allAlerts.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Security Dashboard") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (alerts.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No emergency alerts recorded.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(alerts) { alert ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = when (alert.status) {
                                SosStatus.ACTIVE -> MaterialTheme.colorScheme.errorContainer
                                SosStatus.RESPONDING -> Color(0xFFFFF9C4) // Light Yellow
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = alert.studentName,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Badge(
                                    containerColor = when (alert.status) {
                                        SosStatus.ACTIVE -> Color.Red
                                        SosStatus.RESPONDING -> Color(0xFFFBC02D)
                                        SosStatus.RESOLVED -> Color(0xFF4CAF50)
                                        SosStatus.CANCELLED -> Color.Gray
                                    }
                                ) {
                                    Text(alert.status.name, color = Color.White)
                                }
                            }
                            
                            Text("Reg: ${alert.studentRegNumber}", style = MaterialTheme.typography.bodyMedium)
                            Text("Location: ${alert.location}", style = MaterialTheme.typography.bodyMedium)
                            
                            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                            Text("Time: ${sdf.format(Date(alert.timestamp))}", style = MaterialTheme.typography.bodySmall)
                            
                            if (alert.status == SosStatus.ACTIVE || alert.status == SosStatus.RESPONDING) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    if (alert.status == SosStatus.ACTIVE) {
                                        Button(
                                            onClick = { viewModel.updateAlertStatus(alert.id, SosStatus.RESPONDING) },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Dispatch")
                                        }
                                    }
                                    
                                    Button(
                                        onClick = { viewModel.updateAlertStatus(alert.id, SosStatus.RESOLVED) },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                                    ) {
                                        Text("Resolve")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
