package ug.ac.ndejje.nexus.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ug.ac.ndejje.nexus.model.UniversityData
import ug.ac.ndejje.nexus.model.User
import ug.ac.ndejje.nexus.viewmodel.AuthUiState
import ug.ac.ndejje.nexus.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    viewModel: AuthViewModel,
    onRegistrationSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var regNumber by remember { mutableStateOf("") }
    var faculty by remember { mutableStateOf("") }
    var level by remember { mutableStateOf("") }
    var program by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Dropdown States
    var facultyExpanded by remember { mutableStateOf(value = false) }
    var levelExpanded by remember { mutableStateOf(value = false) }
    var programExpanded by remember { mutableStateOf(value = false) }

    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        if (authState is AuthUiState.RegistrationSuccess) {
            Toast.makeText(context, "Registration Successful! Please login.", Toast.LENGTH_LONG).show()
            viewModel.resetState()
            onRegistrationSuccess()
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
                },
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = regNumber,
                onValueChange = { regNumber = it },
                label = { Text("Registration Number") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Level Dropdown
            ExposedDropdownMenuBox(
                expanded = levelExpanded,
                onExpandedChange = { levelExpanded = !levelExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = level,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Level of Study") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = levelExpanded) },
                    modifier = Modifier.menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = levelExpanded,
                    onDismissRequest = { levelExpanded = false }
                ) {
                    UniversityData.levels.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                level = item
                                levelExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Faculty Dropdown
            ExposedDropdownMenuBox(
                expanded = facultyExpanded,
                onExpandedChange = { facultyExpanded = !facultyExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = faculty,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Faculty") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = facultyExpanded) },
                    modifier = Modifier.menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = facultyExpanded,
                    onDismissRequest = { facultyExpanded = false }
                ) {
                    UniversityData.faculties.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                faculty = item
                                program = "" // Reset program when faculty changes
                                facultyExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Program (Course) Dropdown - Dependent on Faculty
            val courses = UniversityData.facultiesWithCourses[faculty] ?: emptyList()
            ExposedDropdownMenuBox(
                expanded = programExpanded,
                onExpandedChange = { 
                    if (faculty.isNotEmpty()) programExpanded = !programExpanded 
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = program,
                    onValueChange = {},
                    readOnly = true,
                    enabled = faculty.isNotEmpty(),
                    label = { Text("Course of Study") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = programExpanded) },
                    modifier = Modifier.menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = programExpanded,
                    onDismissRequest = { programExpanded = false }
                ) {
                    courses.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                program = item
                                programExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            if (authState is AuthUiState.Error) {
                Text(
                    text = (authState as AuthUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Button(
                onClick = { 
                    viewModel.register(
                        User(
                            name = name,
                            regNumber = regNumber,
                            program = program,
                            faculty = faculty,
                            level = level,
                            email = email,
                            password = password
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = authState !is AuthUiState.Loading
            ) {
                if (authState is AuthUiState.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Register")
                }
            }
        }
    }
}
