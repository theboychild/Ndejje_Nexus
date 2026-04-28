/* 
 * This file contains the "Forgot Password Screen."
 * It provides a way for students to regain access to their account if they 
 * forget their password, by sending a reset link to their university email.
 *
 * HOW MVVM IS USED HERE:
 * 1. VIEW: ForgotPasswordScreen.kt defines the input form and feedback text.
 * 2. MODEL: The "User" data class is conceptually related as it represents 
 *    the account being recovered.
 * 3. VIEW-MODEL: The "AuthViewModel" handles the email-sending logic.
 */
package ug.ac.ndejje.nexus.ui.screens

/* These are the "Tools" we use to build the visual parts of the screen. */
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ug.ac.ndejje.nexus.R
import ug.ac.ndejje.nexus.viewmodel.AuthUiState
import ug.ac.ndejje.nexus.viewmodel.AuthViewModel

/**
 * ForgotPasswordScreen allows students to request a password reset link.
 * 
 * @param viewModel The "Brain" that handles the email request.
 * @param onNavigateBack Action to return to the Login screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel,
    onNavigateBack: () -> Unit,
) {
    /* VIEW USAGE: Tracks the email address entered by the student. */
    var email by remember { mutableStateOf("") }
    
    /* VIEW-MODEL USAGE: We "Watch" the status of the request from the AuthViewModel. */
    val authState by viewModel.authState.collectAsState()

    /* VIEW USAGE: Screen framework with top bar and center-aligned content. */
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Forgot Password") },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            /* INSTRUCTIONS: User guidance text. */
            Text(
                text = "Enter your university email to receive a password reset link.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            /* VIEW USAGE: Text field for user input. */
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email_hint)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            /* VIEW-MODEL USAGE: Button triggers the email reset logic in the ViewModel. */
            if (authState is AuthUiState.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.sendResetEmail(email) },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("Send Reset Link")
                }
            }

            /* VIEW-MODEL USAGE: Error feedback from the ViewModel. */
            if (authState is AuthUiState.Error) {
                Text(
                    text = (authState as AuthUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            
            /* VIEW-MODEL USAGE: Success feedback from the ViewModel. */
            if (authState is AuthUiState.Success) {
                Text(
                    text = "Reset link sent! Please check your email.",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
