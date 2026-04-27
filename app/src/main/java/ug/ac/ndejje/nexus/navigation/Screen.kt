package ug.ac.ndejje.nexus.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object ShuttleTracker : Screen("shuttle_tracker")
    object SOS : Screen("sos")
    object NoticeBoard : Screen("notice_board")
    object Profile : Screen("profile")
    object ForgotPassword : Screen("forgot_password")
    object ShuttleSchedule : Screen("shuttle_schedule")
    object RouteMap : Screen("route_map")
    object SeatReservation : Screen("seat_reservation")
    object SosConfirmation : Screen("sos_confirmation")
    object SafeWalk : Screen("safe_walk")
    object Security : Screen("security")
}