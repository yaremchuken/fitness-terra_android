package exp.yaremchuken.fitnessterra.ui.view.exercise

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import exp.yaremchuken.fitnessterra.bitmap
import exp.yaremchuken.fitnessterra.data.model.EquipmentType
import exp.yaremchuken.fitnessterra.data.model.MuscleGroupType
import exp.yaremchuken.fitnessterra.ui.element.GifImage
import exp.yaremchuken.fitnessterra.ui.theme.AppType
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.uppercaseFirstChar
import exp.yaremchuken.fitnessterra.util.Utils
import exp.yaremchuken.fitnessterra.viewmodel.ExerciseDetailsViewModel

@Composable
fun ExerciseDetailsScreen(
    id: Long,
    viewModel: ExerciseDetailsViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val exercise = viewModel.getExercise(id)!!

    val equipment = LocalContext.current.bitmap("equipment", exercise.equipment?.name)?.asImageBitmap()
    val muscleGroup = LocalContext.current.bitmap("muscle_group", exercise.muscleGroup?.name)?.asImageBitmap()

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
        ) {
            GifImage(
                Utils.exerciseGifPath(exercise),
                Modifier.align(Alignment.Center),
                ContentScale.FillHeight
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
            Modifier
                .padding(horizontal = 20.dp)
                .weight(2F)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = exercise.title,
                Modifier.padding(vertical = 12.dp),
                style = Typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = exercise.description,
                style = Typography.bodyMedium
            )
            if (exercise.equipment != null || exercise.muscleGroup != null) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    if (muscleGroup != null) {
                        Divider()
                        Row(
                            Modifier.padding(vertical = 8.dp)
                        ) {
                            Image(
                                bitmap = muscleGroup,
                                contentDescription = null
                            )
                            Column(
                                Modifier.padding(start = 12.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.muscle_group_title),
                                    style = AppType.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = MuscleGroupType.i18n(exercise.muscleGroup!!).uppercaseFirstChar(),
                                    style = Typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.padding(top = 8.dp))
                                Text(
                                    text = stringResource(id = R.string.involved_muscles_title),
                                    style = AppType.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                exercise.muscles.forEach {
                                    Text(
                                        text = MuscleGroupType.i18n(it).uppercaseFirstChar(),
                                        style = Typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }
                    if (equipment != null) {
                        Divider()
                        Column(
                            Modifier.padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.equipment_title),
                                Modifier.padding(bottom = 8.dp),
                                style = AppType.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    bitmap = equipment,
                                    contentDescription = null,
                                    Modifier
                                        .width(52.dp)
                                        .height(52.dp)
                                )
                                Text(text = EquipmentType.i18n(exercise.equipment!!))
                            }
                        }
                    }
                }
            }
            Divider()
            Text(
                text = stringResource(id = R.string.how_to_do_it),
                Modifier.padding(vertical = 8.dp),
                style = AppType.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Column {
                exercise.steps.forEachIndexed { idx, it ->
                    ExerciseStepView(idx+1, it)
                }
            }
            Spacer(modifier = Modifier.padding(bottom = 8.dp))
            if (exercise.advises.isNotEmpty()) {
                Divider()
                Column(
                    Modifier.padding(vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.advises),
                        Modifier.padding(bottom = 12.dp),
                        style = AppType.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    exercise.advises.forEach {
                        Row(
                            Modifier.padding(bottom = 8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_lamp_icon),
                                contentDescription = null,
                                Modifier
                                    .height(20.dp)
                                    .padding(horizontal = 5.dp)
                            )
                            Text(
                                text = it,
                                style = Typography.bodyMedium
                            )
                        }
                    }
                }
            }
            if (exercise.warnings.isNotEmpty()) {
                Divider()
                Text(
                    text = stringResource(id = R.string.safety_precautions_title),
                    Modifier.padding(top = 8.dp, bottom = 12.dp),
                    style = AppType.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                exercise.warnings.forEach {
                    Row(
                        Modifier.padding(bottom = 8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_warning),
                            contentDescription = null,
                            Modifier
                                .height(20.dp)
                                .padding(horizontal = 5.dp)
                        )
                        Text(
                            text = it,
                            style = Typography.bodyMedium
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(bottom = 20.dp))
        }
    }
}