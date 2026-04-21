package ug.ac.ndejje.nexus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ug.ac.ndejje.nexus.navigation.Screen
import ug.ac.ndejje.nexus.repository.AuthRepository
import ug.ac.ndejje.nexus.ui.screens.*
import ug.ac.ndejje.nexus.ui.theme.NexusTheme
import ug.ac.ndejje.nexus.viewmodel.EmergencyViewModel
import ug.ac.ndejje.nexus.viewmodel.LoginViewModel
import ug.ac.ndejje.nexus.viewmodel.RegistrationViewModel

class MainActivity : ComponentActivity() {
    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NexusTheme {
                NexusApp(authRepository)
            }
        }
    }
}

@Composable
fun NexusApp(authRepository: AuthRepository) {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            val viewModel: LoginViewModel = viewModel(factory = ViewModelFactory(authRepository))
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        
        composable(Screen.Register.route) {
            val viewModel: RegistrationViewModel = viewModel(factory = ViewModelFactory(authRepository))
            RegistrationScreen(
                viewModel = viewModel,
                onRegistrationSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Dashboard.route) {
            val user = authRepository.getCurrentUser()
            if (user != null) {
                DashboardScreen(
                    user = user,
                    onNavigateToSOS = { navController.navigate(Screen.SOS.route) },
                    onNavigateToTracker = { navController.navigate(Screen.ShuttleTracker.route) },
                    onNavigateToNotices = { navController.navigate(Screen.NoticeBoard.route) },
                    onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
                )
            } else {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Dashboard.route) { inclusive = true }
                }
            }
        }

        composable(Screen.SOS.route) {
            val viewModel: EmergencyViewModel = viewModel()
            SOSScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.NoticeBoard.route) {
            NoticeBoardScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ShuttleTracker.route) {
            ShuttleTrackerScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            val user = authRepository.getCurrentUser()
            if (user != null) {
                ProfileScreen(
                    user = user,
                    onNavigateBack = { navController.popBackStack() },
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

class ViewModelFactory(private val repository: AuthRepository) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegistrationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
