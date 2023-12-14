package exp.yaremchuken.fitnessterra.view.workout

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.model.Exercise
import exp.yaremchuken.fitnessterra.model.ExerciseSet
import exp.yaremchuken.fitnessterra.model.Workout
import exp.yaremchuken.fitnessterra.model.WorkoutType
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.utils.Utils
import exp.yaremchuken.fitnessterra.view.exercise.exerciseStub

@Preview
@Composable
fun WorkoutDetailView(
    workout: Workout = workoutStub,
    showExerciseDetails: (exercise: Exercise) -> Unit = {},
    beginWorkout: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    val workoutPreview = Utils.getExercisePreview(LocalContext.current, workout.sets[0].exercise.id)

    // (it.repeats.sum() * 2) - lets make assumption that it takes two seconds for one repetition
    val totalTimeSecs =
        workout.rests.sum() +
        workout.sets.sumOf { it.durations.sum() + it.rests.sum() + (it.repeats.sum() * 2) }

    val workoutStats =
        "${Utils.formatToTime(totalTimeSecs)} • ${workout.sets.size} ${stringResource(id = R.string.exercises_short)}"

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
                    .width(24.dp)
                    .height(24.dp)
            ) {
                Image(painter = painterResource(id = R.drawable.ic_btn_back), contentDescription = null)
            }
        }
        Column(
            Modifier.weight(4F)
        ) {
            Text(
                text = workout.title,
                Modifier.padding(top = 12.dp, start = 20.dp),
                style = Typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = workoutStats,
                Modifier.padding(vertical = 6.dp, horizontal = 20.dp),
                style = Typography.titleMedium
            )
            Divider()
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp)
            ) {
                workout.sets.forEach {
                    ExerciseSetEntryView({ showExerciseDetails(it.exercise) }, it)
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
                onClick = { beginWorkout() },
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 20.dp),
                shape = RoundedCornerShape(12.dp)
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
    title = "Бицепсы",
    type = WorkoutType.STRENGTH,
    sets = listOf(
        ExerciseSet(
            exercise = exerciseStub,
            weight = 500,
            repeats = listOf(10, 10, 10),
            durations = listOf(),
            rests = listOf(30, 30)
        ),
        ExerciseSet(
            exercise = exerciseStub,
            weight = 500,
            repeats = listOf(10, 10, 10),
            durations = listOf(),
            rests = listOf(30, 30)
        )
    ),
    rests = listOf(60)
)