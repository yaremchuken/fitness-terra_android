package exp.yaremchuken.fitnessterra.ui.view.library.exercise

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
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.Exercise
import exp.yaremchuken.fitnessterra.data.model.MuscleGroupType
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.uppercaseFirstChar
import exp.yaremchuken.fitnessterra.viewmodel.ExerciseLibraryViewModel

@Composable
fun ExerciseLibraryScreen(
    gotoExercise: (exerciseId: Long) -> Unit,
    viewModel: ExerciseLibraryViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val exercises = remember { mutableStateListOf<Exercise>() }

    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    LaunchedEffect(Unit) {
        exercises.addAll(viewModel.getExercises())
    }

    val muscleGroups = exercises.map { it.muscleGroup }.filterNotNull().distinct()

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
        ) {
            IconButton(
                onClick = { onBackPressedDispatcher?.onBackPressed() },
                Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 6.dp, bottom = 6.dp, start = 12.dp)
                    .width(30.dp)
                    .height(30.dp)
            ) {
                Image(painter = painterResource(id = R.drawable.ic_back_filled), contentDescription = null)
            }
            Text(
                text = stringResource(R.string.exercise_library_title),
                Modifier.align(Alignment.Center),
                style = Typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        }
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            muscleGroups.forEach { group ->
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = MuscleGroupType.i18n(group).uppercaseFirstChar(),
                        Modifier.padding(top = 12.dp),
                        style = Typography.headlineMedium
                    )
                    Divider()
                    exercises
                        .filter { ex -> ex.muscleGroup == group }
                        .forEach { ex ->
                                ExerciseBlock(
                                onClick = { gotoExercise(ex.id) },
                                exercise = ex
                            )
                        }
                }
            }
        }
    }
}