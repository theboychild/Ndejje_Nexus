package ug.ac.ndejje.nexus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ug.ac.ndejje.nexus.viewmodel.SosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SafeWalkScreen(
    viewModel: SosViewModel,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Safe-Walk Timer") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        SafeWalkContent(viewModel = viewModel, modifier = Modifier.padding(padding))
    }
}

@Composable
fun SafeWalkContent(viewModel: SosViewModel, modifier: Modifier = Modifier) {
    var timerRunning by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableIntStateOf(600) } 

    LaunchedEffect(timerRunning) {
        if (timerRunning) {
            while (timeLeft > 0) {
                kotlinx.coroutines.delay(1000)
                timeLeft--
            }
            if (timeLeft == 0) {
                viewModel.triggerSos()
                timerRunning = false
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = if (timerRunning) "Timer Active" else "Set Safe-Walk Timer", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60), style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(48.dp))
        if (!timerRunning) {
            Button(onClick = { timerRunning = true }, modifier = Modifier.fillMaxWidth().height(56.dp)) { Text("Start Timer") }
        } else {
            Button(onClick = { timerRunning = false; timeLeft = 600 }, modifier = Modifier.fillMaxWidth().height(56.dp)) { Text("I Am Safe (Stop)") }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "If the timer reaches zero, an SOS alert will be sent automatically.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
    }
}
