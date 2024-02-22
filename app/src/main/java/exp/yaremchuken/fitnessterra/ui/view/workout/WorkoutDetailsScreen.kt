package exp.yaremchuken.fitnessterra.ui.view.workout

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.EquipmentType
import exp.yaremchuken.fitnessterra.data.model.History
import exp.yaremchuken.fitnessterra.data.model.Workout
import exp.yaremchuken.fitnessterra.equipment
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.theme.AppType
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.util.Utils
import exp.yaremchuken.fitnessterra.viewmodel.WorkoutDetailsViewModel
import java.time.Instant

@Composable
fun WorkoutDetailsScreen(
    gotoExerciseSetupDetails: (sectionId: Long, exerciseId: Long) -> Unit,
    gotoExerciseDetails: (exerciseId: Long) -> Unit,
    beginWorkout: (workoutId: Long) -> Unit,
    workoutId: Long? = null,
    startedAt: Instant? = null,
    viewModel: WorkoutDetailsViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val equipmentScrollState = rememberScrollState()

    var workout by remember { mutableStateOf<Workout?>(null) }
    var history by remember { mutableStateOf<History?>(null) }

    LaunchedEffect(Unit) {
        if (workoutId != null) {
            viewModel.getWorkout(workoutId).collect { workout = viewModel.fromEntity(it) }
        } else if (startedAt != null) {
            viewModel.getHistory(startedAt).collect {
                history = viewModel.fromEntity(it)
                workout = history!!.workout
            }
        } else {
            throw IllegalArgumentException("Workout.id or history.startedAt must be provided")
        }
    }

    if (workout == null) {
        return
    }

    val workoutPreview = viewModel.getPreview(workout!!, LocalContext.current)

    val workoutStats =
        "${Utils.formatToTime(workout!!.totalDuration())} • " +
        "${workout!!.sections.sumOf { it.setups.size }} ${stringResource(id = R.string.exercises_count_title)}"

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
        Divider()
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
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp)
            ) {
                if (viewModel.equipment(workout!!).isNotEmpty()) {
                    Text(
                        text = stringResource(id = R.string.equipment_title),
                        style = AppType.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Row (
                        Modifier.horizontalScroll(equipmentScrollState)
                    ) {
                        viewModel.equipment(workout!!).forEachIndexed { idx, it ->
                            Column(
                                Modifier.padding(
                                    top = 8.dp,
                                    bottom = 8.dp,
                                    start = if (idx == 0) 0.dp else 8.dp
                                ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    bitmap = LocalContext.current.equipment(it)!!.asImageBitmap(),
                                    contentDescription = null,
                                    Modifier
                                        .width(52.dp)
                                        .height(52.dp)
                                )
                                Text(text = EquipmentType.i18n(it))
                            }
                        }
                    }
                    Divider()
                }
                workout!!.sections.forEach { section ->
                    Text(
                        text = section.title,
                        Modifier.padding(top = 12.dp),
                        style = Typography.titleLarge
                    )
                    section.setups.forEach {
                        ExerciseSetupBlock(
                            {
                                if (workoutId == null) gotoExerciseDetails(it.exercise.id)
                                else gotoExerciseSetupDetails(section.id, it.exercise.id)
                            },
                            it
                        )
                    }
                }
            }
        }
        if (workoutId != null) {
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
}
