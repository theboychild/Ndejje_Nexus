package ug.ac.ndejje.nexus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ug.ac.ndejje.nexus.model.User
import ug.ac.ndejje.nexus.viewmodel.SosUiState
import ug.ac.ndejje.nexus.viewmodel.SosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SOSScreen(
    user: User,
    viewModel: SosViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToConfirmation: () -> Unit
) {
    val sosState by viewModel.sosState.collectAsState()
    
    LaunchedEffect(sosState) {
        if (sosState is SosUiState.Sent) {
            onNavigateToConfirmation()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emergency") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        SOSContent(user = user, viewModel = viewModel, modifier = Modifier.padding(padding))
    }
}

@Composable
fun SOSContent(user: User, viewModel: SosViewModel, modifier: Modifier = Modifier) {
    val sosState by viewModel.sosState.collectAsState()

    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val state = sosState) {
            is SosUiState.Sending -> {
                CircularProgressIndicator(modifier = Modifier.size(64.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text("Contacting Security...")
            }
            is SosUiState.Error -> {
                Text(text = state.message, color = MaterialTheme.colorScheme.error)
                Button(onClick = { viewModel.triggerSos(user) }) { Text("Retry") }
            }
            else -> {
                Text(text = "Press to trigger emergency alert", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { viewModel.triggerSos(user) },
                    modifier = Modifier.size(200.dp),
                    shape = androidx.compose.foundation.shape.CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(text = "SOS", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
