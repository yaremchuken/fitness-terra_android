package exp.yaremchuken.fitnessterra.view.exercise

import android.graphics.Bitmap
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.EXTENSION_PNG
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.bitmap
import exp.yaremchuken.fitnessterra.bitmaps
import exp.yaremchuken.fitnessterra.model.EquipmentType
import exp.yaremchuken.fitnessterra.model.Exercise
import exp.yaremchuken.fitnessterra.model.MuscleGroup
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.utils.Utils

@Preview
@Composable
fun ExerciseDetailView(
    exercise: Exercise = exerciseStub
) {
    val scrollState = rememberScrollState()
    val visualStatesScrollState = rememberScrollState()

    val bitmaps = LocalContext.current.bitmaps("exercise/${exercise.id}")
    val preview = Utils.getExercisePreview(LocalContext.current, exercise.id)

    val equipment = LocalContext.current.bitmap("equipment", exercise.equipment?.name)?.asImageBitmap()
    val muscleGroup = LocalContext.current.bitmap("muscle_group", exercise.muscleGroup?.name)?.asImageBitmap()

    val visualSteps = ArrayList<Bitmap>()
    for (i in 0 until bitmaps.size) {
        val key = "step_$i$EXTENSION_PNG"
        if (bitmaps.containsKey(key)) {
            visualSteps.add(bitmaps[key]!!)
        } else break
    }

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
                .background(Utils.getBackgroundTint(preview))
        ) {
            Image(
                preview,
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
        Text(
            text = exercise.title,
            Modifier.padding(vertical = 12.dp, horizontal = 20.dp),
            style = Typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Column(
            Modifier
                .padding(horizontal = 20.dp)
                .weight(4F)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = exercise.description ?: "",
                style = Typography.bodyMedium
            )
            if (exercise.equipment != null || exercise.muscleGroup != null) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    if (equipment != null) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(id = R.string.equipment_title),
                                Modifier.padding(bottom = 8.dp),
                                style = Typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Image(
                                bitmap = equipment,
                                contentDescription = null,
                                Modifier
                                    .width(64.dp)
                                    .height(64.dp)
                            )
                            Text(text = EquipmentType.i18n(exercise.equipment!!))
                        }
                    }
                    if (muscleGroup != null) {
                        Column(
                            Modifier.padding(start = if (equipment != null) 20.dp else 0.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(id = R.string.muscle_group_title),
                                Modifier.padding(bottom = 8.dp),
                                style = Typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Image(
                                bitmap = muscleGroup,
                                contentDescription = null,
                                Modifier
                                    .width(64.dp)
                                    .height(64.dp)
                            )
                            Text(text = MuscleGroup.i18n(exercise.muscleGroup!!))
                        }
                    }
                }
            }
            Text(
                text = stringResource(id = R.string.how_to_do_it),
                Modifier.padding(vertical = 12.dp),
                style = Typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            if (visualSteps.isNotEmpty()) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .heightIn(max = 180.dp)
                        .horizontalScroll(visualStatesScrollState)
                ) {
                    visualSteps.forEachIndexed { idx, it ->
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null,
                            Modifier.padding(
                                start = if (idx == 0) 0.dp else 8.dp
                            )
                        )
                    }
                }
            }
            Column {
                exercise.steps.forEachIndexed { idx, it ->
                    ExerciseStepView(idx+1, it)
                }
            }
            if (exercise.advise != null) {
                Text(
                    text = stringResource(id = R.string.advises),
                    Modifier.padding(vertical = 12.dp),
                    style = Typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = exercise.advise,
                    style = Typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.padding(bottom = 12.dp))
        }
    }
}

val exerciseStub = Exercise(
    id = 0,
    title = "Сгибание на бицепс стоя",
    description = "Данное упражнение является самым популярным упражнением на мышцы бицепса. Упражнения изолирующее. Может выполняться как со штангой, так и с гантелями. Существует разновидность упражнения, когда вместо обычно штанги используется штанга с изогнутым грифом (более эффективный и безопасный способ).",
    muscleGroup = MuscleGroup.CHEST,
    equipment = EquipmentType.BARBELL,
    steps = listOf(
        "Встаньте прямо, предварительно взяв в руку штангу. Руки должны находится на уровне плеч. Хват снизу. Локти находятся близко к телу. Это будет вашей стартовой позицией",
        "Держа верхние отделы рук в неподвижном состоянии, выдохните и согните руки в локтях усилиями бицепса. В данном движении участвуют только нижние отделы руки (предплечье)",
        "Продолжайте упражнения до того положения, когда ваши руки будут находится на уровне плеч. В верхней точке задержитесь на несколько секунд и максимально напрягите бицепс",
        "Вдохните и медленно начните опускать штангу в стартовое положение",
        "Выполните движение необходимо количество раз"
    ),
    advise = "Вы можете попробовать выполнение данного упражнения узким хватом.")