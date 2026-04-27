/* 
 * This is the "Main Activity." 
 * Think of it as the "Front Door" or the "Engine Room" of the entire app. 
 */
package ug.ac.ndejje.nexus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ug.ac.ndejje.nexus.navigation.Screen
import ug.ac.ndejje.nexus.repository.*
import ug.ac.ndejje.nexus.model.User
import ug.ac.ndejje.nexus.ui.screens.*
import ug.ac.ndejje.nexus.ui.theme.NexusTheme
import ug.ac.ndejje.nexus.viewmodel.*

class MainActivity : ComponentActivity() {
    private val authRepository = AuthRepository()
    private val noticeRepository = NoticeRepository()
    private val shuttleRepository = ShuttleRepository()
    private val sosRepository = SosRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NexusTheme {
                NexusApp(authRepository, noticeRepository, shuttleRepository, sosRepository)
            }
        }
    }
}

/**
 * NexusApp is the "Director" or "Traffic Controller" of the app.
 */
@Composable
fun NexusApp(
    authRepository: AuthRepository,
    noticeRepository: NoticeRepository,
    shuttleRepository: ShuttleRepository,
    sosRepository: SosRepository
) {
    val navController = rememberNavController()
    val factory = ViewModelFactory(authRepository, noticeRepository, shuttleRepository, sosRepository)
    
    /* We initialize the ViewModels. */
    val authViewModel: AuthViewModel = viewModel(factory = factory)
    val dashboardViewModel: DashboardViewModel = viewModel(factory = factory)
    val shuttleViewModel: ShuttleViewModel = viewModel(factory = factory)
    val sosViewModel: SosViewModel = viewModel(factory = factory)
    val profileViewModel: ProfileViewModel = viewModel()
    
    /* We "Watch" the current user from the repository. */
    val currentUser by authRepository.currentUser.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = when (currentDestination?.route) {
        Screen.Login.route -> false
        Screen.Register.route -> false
        Screen.ForgotPassword.route -> false
        Screen.Security.route -> false
        Screen.Transport.route -> false
        null -> false
        else -> true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    tonalElevation = 8.dp
                ) {
                    val items = listOf(
                        Triple("Home", Screen.Dashboard.route, Icons.Default.Home),
                        Triple("Shuttle", Screen.ShuttleTracker.route, Icons.Default.DirectionsBus),
                        Triple("SOS", Screen.SOS.route, Icons.Default.Warning),
                        Triple("Notices", Screen.NoticeBoard.route, Icons.Default.Notifications),
                        Triple("Profile", Screen.Profile.route, Icons.Default.Person)
                    )
                    items.forEach { (label, route, icon) ->
                        NavigationBarItem(
                            icon = { Icon(icon, contentDescription = label) },
                            label = { Text(label) },
                            selected = currentDestination?.hierarchy?.any { it.route == route } == true,
                            onClick = {
                                navController.navigate(route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController, 
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            
            /* 2. LOGIN SCREEN. */
            composable(Screen.Login.route) {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    },
                    onNavigateToForgotPassword = {
                        navController.navigate(Screen.ForgotPassword.route)
                    },
                    onNavigateToSecurity = {
                        navController.navigate(Screen.Security.route)
                    },
                    onNavigateToTransport = {
                        navController.navigate(Screen.Transport.route)
                    }
                )
            }
            
            /* 3. REGISTRATION SCREEN. */
            composable(Screen.Register.route) {
                RegistrationScreen(
                    viewModel = authViewModel,
                    onRegistrationSuccess = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            /* 4. FORGOT PASSWORD SCREEN. */
            composable(Screen.ForgotPassword.route) {
                ForgotPasswordScreen(
                    viewModel = authViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            /* 5. DASHBOARD: Home base. */
            composable(Screen.Dashboard.route) {
                val user = currentUser
                if (user != null) {
                    DashboardScreen(
                        user = user,
                        viewModel = dashboardViewModel,
                        onNavigateToSOS = { navController.navigate(Screen.SOS.route) },
                        onNavigateToTracker = { navController.navigate(Screen.ShuttleTracker.route) },
                        onNavigateToNotices = { navController.navigate(Screen.NoticeBoard.route) },
                        onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
                    )
                } else {
                    LaunchedEffect(Unit) {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Dashboard.route) { inclusive = true }
                        }
                    }
                }
            }

            /* 6. NOTICE BOARD. */
            composable(Screen.NoticeBoard.route) {
                NoticeBoardScreen(
                    viewModel = dashboardViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            /* 7. SHUTTLE HUB. */
            composable(Screen.ShuttleTracker.route) {
                ShuttleHubScreen(
                    viewModel = shuttleViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            /* 8. EMERGENCY HUB. */
            composable(Screen.SOS.route) {
                val user = currentUser
                if (user != null) {
                    EmergencyHubScreen(
                        user = user,
                        viewModel = sosViewModel,
                        onNavigateToConfirmation = {
                            navController.navigate(Screen.SosConfirmation.route) {
                                popUpTo(Screen.SOS.route) { inclusive = true }
                            }
                        },
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }

            /* 9. SOS CONFIRMATION. */
            composable(Screen.SosConfirmation.route) {
                val user = currentUser
                if (user != null) {
                    SosConfirmationScreen(
                        user = user,
                        viewModel = sosViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }

            /* 10. SHUTTLE SCHEDULE. */
            composable(Screen.ShuttleSchedule.route) {
                ShuttleScheduleScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            /* 11. ROUTE MAP. */
            composable(Screen.RouteMap.route) {
                RouteMapScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            /* 12. SEAT RESERVATION. */
            composable(Screen.SeatReservation.route) {
                val user = currentUser
                if (user != null) {
                    SeatReservationScreen(
                        user = user,
                        viewModel = shuttleViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }

            /* 13. SAFE WALK. */
            composable(Screen.SafeWalk.route) {
                val user = currentUser
                if (user != null) {
                    SafeWalkScreen(
                        user = user,
                        viewModel = sosViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }

            /* 14. PROFILE SCREEN. */
            composable(Screen.Profile.route) {
                val user = currentUser
                if (user != null) {
                    ProfileScreen(
                        user = user,
                        viewModel = profileViewModel,
                        onNavigateBack = { navController.popBackStack() },
                        onLogout = {
                            authViewModel.logout()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                } else {
                    LaunchedEffect(Unit) {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            }

            /* 15. SECURITY SCREEN. */
            composable(Screen.Security.route) {
                SecurityScreen(
                    viewModel = sosViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            /* 16. TRANSPORT SCREEN (Driver). */
            composable(Screen.Transport.route) {
                TransportScreen(
                    viewModel = shuttleViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

/**
 * ViewModelFactory helps create ViewModels that need dependencies.
 */
class ViewModelFactory(
    private val authRepository: AuthRepository,
    private val noticeRepository: NoticeRepository,
    private val shuttleRepository: ShuttleRepository,
    private val sosRepository: SosRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> 
                AuthViewModel(authRepository) as T
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> 
                DashboardViewModel(noticeRepository, shuttleRepository) as T
            modelClass.isAssignableFrom(ShuttleViewModel::class.java) -> 
                ShuttleViewModel(shuttleRepository) as T
            modelClass.isAssignableFrom(SosViewModel::class.java) -> 
                SosViewModel(sosRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
