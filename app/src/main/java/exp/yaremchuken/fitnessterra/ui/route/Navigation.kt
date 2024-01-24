package exp.yaremchuken.fitnessterra.ui.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import exp.yaremchuken.fitnessterra.ui.view.exercise.ExerciseDetailsScreen
import exp.yaremchuken.fitnessterra.ui.view.home.HomeScreen
import exp.yaremchuken.fitnessterra.ui.view.library.exercise.ExerciseLibraryScreen
import exp.yaremchuken.fitnessterra.ui.view.library.workout.WorkoutLibraryScreen
import exp.yaremchuken.fitnessterra.ui.view.perform.WorkoutPerformScreen
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
                gotoCalendar = { navController.navigate(Screen.SCHEDULE_CALENDAR_SCREEN.name) },
                gotoExerciseLibrary = { navController.navigate(Screen.EXERCISE_LIBRARY_SCREEN.name) },
                gotoWorkoutLibrary = { navController.navigate(Screen.WORKOUT_LIBRARY_SCREEN.name) },
                gotoWorkout = { id -> navController.navigate("${Screen.WORKOUT_DETAILS_SCREEN.name}/$id") }
            )
        }
        composable(route = Screen.SCHEDULE_CALENDAR_SCREEN.name) {
            ScheduleCalendarScreen(
                gotoCalendarDate = { date -> navController.navigate("${Screen.SCHEDULE_DATE_SCREEN.name}/${date.format(ISO_DATE)}") }
            )
        }
        composable(route = Screen.EXERCISE_LIBRARY_SCREEN.name) {
            ExerciseLibraryScreen(
                gotoExercise = { id -> navController.navigate("${Screen.EXERCISE_DETAILS_SCREEN.name}/$id") }
            )
        }
        composable(route = Screen.WORKOUT_LIBRARY_SCREEN.name) {
            WorkoutLibraryScreen(
                gotoWorkout = { id -> navController.navigate("${Screen.WORKOUT_DETAILS_SCREEN.name}/$id") }
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
                onWorkoutDetailsClick = { id -> navController.navigate("${Screen.WORKOUT_DETAILS_SCREEN.name}/$id") },
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
                showExerciseDetails = { id -> navController.navigate("${Screen.EXERCISE_DETAILS_SCREEN.name}/$id") },
                beginWorkout = { id -> navController.navigate("${Screen.WORKOUT_PERFORM_SCREEN}/$id") },
                workoutId = it.arguments!!.getInt("id").toLong()
            )
        }
        composable(
            route = Screen.EXERCISE_DETAILS_SCREEN.name + "/{id}",
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) {
            ExerciseDetailsScreen(it.arguments!!.getInt("id").toLong())
        }
        composable(
            route = Screen.WORKOUT_PERFORM_SCREEN.name + "/{id}",
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) {
            WorkoutPerformScreen(
                goHome = { navController.navigate(Screen.HOME_SCREEN.name) },
                it.arguments!!.getInt("id").toLong()
            )
        }
    }
}