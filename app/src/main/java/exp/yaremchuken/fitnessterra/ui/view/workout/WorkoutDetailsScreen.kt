package exp.yaremchuken.fitnessterra.ui.view.workout

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.ExerciseSet
import exp.yaremchuken.fitnessterra.data.model.Workout
import exp.yaremchuken.fitnessterra.data.model.WorkoutSection
import exp.yaremchuken.fitnessterra.data.model.WorkoutType
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.theme.AppType
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.ui.view.exercise.exerciseBackStub
import exp.yaremchuken.fitnessterra.ui.view.exercise.exerciseBicepsStub
import exp.yaremchuken.fitnessterra.util.Utils
import exp.yaremchuken.fitnessterra.viewmodel.WorkoutDetailsViewModel
import kotlin.time.Duration.Companion.seconds

@Composable
fun WorkoutDetailsScreen(
    showExerciseDetails: (exerciseId: Long) -> Unit,
    beginWorkout: (workoutId: Long) -> Unit,
    workoutId: Long,
    viewModel: WorkoutDetailsViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val workout by remember { mutableStateOf(viewModel.getWorkout(workoutId)) }

    val workoutPreview = viewModel.getPreview(workout!!, LocalContext.current)

    val workoutStats =
        "${Utils.formatToTime(workout!!.totalDuration())} • " +
        "${workout!!.sections.sumOf { it.sets.size }} ${stringResource(id = R.string.exercises_count_title)}"

    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1F)
                .background(Utils.getBackgroundTint(workoutPreview))
        ) {
            Image(
                workoutPreview,
                null,
                Modifier.align(Alignment.Center),
                contentScale = ContentScale.FillWidth
            )
            IconButton(
                onClick = { onBackPressedDispatcher?.onBackPressed() },
                Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 12.dp, start = 12.dp)
                    .width(30.dp)
                    .height(30.dp)
            ) {
                Image(painter = painterResource(id = R.drawable.ic_back_filled), contentDescription = null)
            }
        }
        Column(
            Modifier.weight(3F)
        ) {
            Text(
                text = workout!!.title,
                Modifier.padding(top = 12.dp, start = 20.dp),
                style = Typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = workoutStats,
                Modifier.padding(vertical = 6.dp, horizontal = 20.dp),
                style = AppType.titleMedium
            )
            Divider()
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp)
            ) {
                workout!!.sections.forEach { section ->
                    Text(
                        text = section.title,
                        Modifier.padding(top = 12.dp),
                        style = Typography.titleLarge
                    )
                    section.sets.forEach {
                        ExerciseSetEntryView({ showExerciseDetails(it.exercise.id) }, it)
                    }
                }
            }
        }
        Column (
            Modifier
                .fillMaxWidth()
                .background(Color.LightGray),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { beginWorkout(workoutId) },
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 50.dp),
                shape = UIConstants.ROUNDED_CORNER
            ) {
                Text(
                    text = stringResource(id = R.string.start_btn_title),
                    style = Typography.titleLarge
                )
            }
        }
    }
}

val workoutStub = Workout(
    id = 0,
    title = "Спина и бицепс",
    type = WorkoutType.STRENGTH,
    sections = listOf(
        WorkoutSection(
            "Спина",
            listOf(
                ExerciseSet(
                    exercise = exerciseBackStub(),
                    weight = 1200,
                    repeats = listOf(12,10,10,8,6),
                    recovery = 30.seconds
                )
            )
        ),
        WorkoutSection(
            "Бицепс",
            listOf(
                ExerciseSet(
                    exercise = exerciseBicepsStub(),
                    weight = 500,
                    repeats = listOf(10,10,10),
                    recovery = 30.seconds
                ),
                ExerciseSet(
                    exercise = exerciseBicepsStub(),
                    weight = 500,
                    repeats = listOf(10,10,10),
                    recovery = 30.seconds
                )
            )
        )
    )
)