package ug.ac.ndejje.nexus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ug.ac.ndejje.nexus.model.User
import ug.ac.ndejje.nexus.viewmodel.AuthUiState
import ug.ac.ndejje.nexus.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    viewModel: AuthViewModel,
    onRegistrationSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var regNumber by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var faculty by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthUiState.Success) {
            onRegistrationSuccess()
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Account") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = regNumber, onValueChange = { regNumber = it }, label = { Text("Registration Number") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = course, onValueChange = { course = it }, label = { Text("Course of Study") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = faculty, onValueChange = { faculty = it }, label = { Text("Faculty") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email Address") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())
            
            Spacer(modifier = Modifier.height(32.dp))

            if (authState is AuthUiState.Error) {
                Text(text = (authState as AuthUiState.Error).message, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 16.dp))
            }

            Button(
                onClick = { 
                    viewModel.register(User(name, regNumber, course, faculty, password, email))
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = authState !is AuthUiState.Loading
            ) {
                if (authState is AuthUiState.Loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("Register")
                }
            }
        }
    }
}
