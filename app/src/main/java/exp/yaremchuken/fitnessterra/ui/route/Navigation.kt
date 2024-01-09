package exp.yaremchuken.fitnessterra.ui.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import exp.yaremchuken.fitnessterra.ui.view.home.HomeScreen
import exp.yaremchuken.fitnessterra.ui.view.schedule.calendar.ScheduleCalendarScreen
import exp.yaremchuken.fitnessterra.ui.view.schedule.date.ScheduleDateScreen
import java.time.LocalDate

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.HOME_SCREEN.name) {
        composable(route = Screen.HOME_SCREEN.name) {
            HomeScreen(navController)
        }
        composable(route = Screen.SCHEDULE_CALENDAR_SCREEN.name) {
            ScheduleCalendarScreen(navController)
        }
        composable(
            route = Screen.SCHEDULE_DATE_SCREEN.name + "/{date}",
            arguments = listOf(
                navArgument(name = "date") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) {
            ScheduleDateScreen(
                navController,
                date = LocalDate.parse(it.arguments?.getString("date"))
            )
        }
    }
}