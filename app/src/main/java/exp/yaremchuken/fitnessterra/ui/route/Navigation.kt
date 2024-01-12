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
import exp.yaremchuken.fitnessterra.ui.view.workout.WorkoutDetailsScreen
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ISO_DATE

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.HOME_SCREEN.name) {
        composable(route = Screen.HOME_SCREEN.name) {
            HomeScreen(
                { navController.navigate(Screen.SCHEDULE_CALENDAR_SCREEN.name) },
                { navController.navigate("${Screen.WORKOUT_DETAILS_SCREEN.name}/$it") }
            )
        }
        composable(route = Screen.SCHEDULE_CALENDAR_SCREEN.name) {
            ScheduleCalendarScreen(
                { navController.navigate("${Screen.SCHEDULE_DATE_SCREEN.name}/${it.format(ISO_DATE)}") }
            )
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
                { throw NotImplementedError() },
                date = LocalDate.parse(it.arguments!!.getString("date"))
            )
        }
        composable(
            route = Screen.WORKOUT_DETAILS_SCREEN.name + "/{id}",
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) {
            WorkoutDetailsScreen(
                showExerciseDetails = { throw NotImplementedError() },
                beginWorkout = { throw NotImplementedError() },
                workoutId = it.arguments!!.getInt("id").toLong()
            )
        }
    }
}