package exp.yaremchuken.fitnessterra.ui.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import exp.yaremchuken.fitnessterra.ui.view.exercise.ExerciseDetailsScreen
import exp.yaremchuken.fitnessterra.ui.view.exercisesetup.ExerciseSetupDetailsScreen
import exp.yaremchuken.fitnessterra.ui.view.home.HomeScreen
import exp.yaremchuken.fitnessterra.ui.view.library.exercise.ExerciseLibraryScreen
import exp.yaremchuken.fitnessterra.ui.view.library.workout.WorkoutLibraryScreen
import exp.yaremchuken.fitnessterra.ui.view.perform.WorkoutPerformScreen
import exp.yaremchuken.fitnessterra.ui.view.schedule.calendar.ScheduleCalendarScreen
import exp.yaremchuken.fitnessterra.ui.view.schedule.date.ScheduleDateScreen
import exp.yaremchuken.fitnessterra.ui.view.workout.WorkoutDetailsScreen
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ISO_DATE

private object Arg {
    val id =
        navArgument(name = "id") {
            type = NavType.LongType
            nullable = false
        }
    val date =
        navArgument(name = "date") {
            type = NavType.StringType
            nullable = false
        }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.HOME_SCREEN.name) {
        composable(route = Screen.HOME_SCREEN.name) {
            HomeScreen(
                gotoCalendar = { navController.navigate(Screen.SCHEDULE_CALENDAR_SCREEN.name) },
                gotoExerciseLibrary = { navController.navigate(Screen.EXERCISE_LIBRARY_SCREEN.name) },
                gotoWorkoutLibrary = { navController.navigate(Screen.WORKOUT_LIBRARY_SCREEN.name) },
                gotoWorkout = { id -> navController.navigate("${Screen.WORKOUT_DETAILS_SCREEN.name}?workoutId=$id") },
                gotoHistory = { startedAt -> navController.navigate("${Screen.WORKOUT_DETAILS_SCREEN.name}?startedAt=${startedAt.toEpochMilli()}") }
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
                gotoWorkout = { id -> navController.navigate("${Screen.WORKOUT_DETAILS_SCREEN.name}?workoutId=$id") }
            )
        }
        composable(
            route = Screen.SCHEDULE_DATE_SCREEN.name + "/{date}",
            arguments = listOf(Arg.date)
        ) {
            ScheduleDateScreen(
                onWorkoutDetailsClick = { id -> navController.navigate("${Screen.WORKOUT_DETAILS_SCREEN.name}?workoutId=$id") },
                date = LocalDate.parse(it.arguments!!.getString("date"))
            )
        }
        composable(
            route = Screen.WORKOUT_DETAILS_SCREEN.name + "?workoutId={workoutId}&startedAt={startedAt}",
            arguments = listOf(
                navArgument(name = "workoutId") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(name = "startedAt") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            WorkoutDetailsScreen(
                gotoExerciseSetupDetails = { sectionId, exerciseId -> navController.navigate("${Screen.EXERCISE_SETUP_DETAILS_SCREEN.name}?sectionId=$sectionId&exerciseId=$exerciseId") },
                gotoExerciseDetails = { id -> navController.navigate("${Screen.EXERCISE_DETAILS_SCREEN.name}/$id") },
                beginWorkout = { id -> navController.navigate("${Screen.WORKOUT_PERFORM_SCREEN}/$id") },
                workoutId = it.arguments?.getString("workoutId")?.toLong(),
                startedAt = it.arguments?.getString("startedAt")?.toLong()?.let { i -> Instant.ofEpochMilli(i) }
            )
        }
        composable(
            route = Screen.EXERCISE_SETUP_DETAILS_SCREEN.name + "?sectionId={sectionId}&exerciseId={exerciseId}",
            arguments = listOf(
                navArgument(name = "sectionId") {
                    type = NavType.LongType
                    nullable = false
                },
                navArgument(name = "exerciseId") {
                    type = NavType.LongType
                    nullable = false
                }
            )
        ) {
            ExerciseSetupDetailsScreen(
                gotoExercise = { id -> navController.navigate("${Screen.EXERCISE_DETAILS_SCREEN.name}/$id") },
                sectionId = it.arguments!!.getLong("sectionId"),
                exerciseId = it.arguments!!.getLong("exerciseId")
            )
        }
        composable(
            route = Screen.EXERCISE_DETAILS_SCREEN.name + "/{id}",
            arguments = listOf(Arg.id)
        ) {
            ExerciseDetailsScreen(it.arguments!!.getLong("id"))
        }
        composable(
            route = Screen.WORKOUT_PERFORM_SCREEN.name + "/{id}",
            arguments = listOf(Arg.id)
        ) {
            WorkoutPerformScreen(
                goHome = {
                            navController.popBackStack(Screen.HOME_SCREEN.name, true)
                            navController.navigate(Screen.HOME_SCREEN.name)
                         },
                workoutId = it.arguments!!.getLong("id")
            )
        }
    }
}