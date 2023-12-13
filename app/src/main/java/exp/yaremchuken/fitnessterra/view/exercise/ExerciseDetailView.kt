package exp.yaremchuken.fitnessterra.view.exercise

import android.graphics.Bitmap
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.bitmap
import exp.yaremchuken.fitnessterra.bitmaps
import exp.yaremchuken.fitnessterra.model.EquipmentType
import exp.yaremchuken.fitnessterra.model.Exercise
import exp.yaremchuken.fitnessterra.model.MuscleGroup
import exp.yaremchuken.fitnessterra.ui.theme.Typography

@Preview
@Composable
fun ExerciseDetailView(
    exercise: Exercise = exerciseStub
) {
    val scrollState = rememberScrollState()
    val visualStatesScrollState = rememberScrollState()

    val bitmaps = LocalContext.current.bitmaps("exercise/${exercise.id}")
    val preview = (bitmaps["preview.png"] ?: LocalContext.current.bitmap("exercise/preview_default.jpg")).asImageBitmap()

    val visualSteps = ArrayList<Bitmap>()
    for (i in 0 until bitmaps.size) {
        val key = "step_$i.png"
        if (bitmaps.containsKey(key)) {
            visualSteps.add(bitmaps[key]!!)
        } else break
    }

    val buffer = IntArray(preview.height * preview.width)
    preview.readPixels(buffer)
    val bgTint =
        buffer[0] +
        buffer[preview.width] +
        buffer[preview.height * preview.width - 1] +
        buffer[preview.height * preview.width - 1 - preview.width]

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
                .background(Color(bgTint))
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
                    .padding(top = 5.dp, end = 5.dp)
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
                .weight(3F)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = exercise.description ?: "",
                style = Typography.bodyMedium
            )
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

@Preview
@Composable
fun ExerciseStepView(
    index: Int = 1,
    description: String = exerciseStub.steps[0]
) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 14f), 0f)

    Row(
        Modifier
            .fillMaxWidth()
            .padding(
                top = if (index == 1) 0.dp else 8.dp
            )
    ) {
        Column {
            Text(
                text = "$index".padStart(2, '0'),
                style = Typography.bodyLarge,
                color = Color.Magenta
            )
        }
        Column {
            Image(
                painter = painterResource(id = R.drawable.ic_exercise_steps_divider),
                contentDescription = null,
                Modifier.padding(horizontal = 5.dp)
            )
            Canvas(
                Modifier
                    .height(40.dp)
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(vertical = 5.dp)
            ) {
                drawLine(
                    color = Color.Magenta,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    pathEffect = pathEffect
                )
            }
//            Image(
//                painter = painterResource(id = R.drawable.img_dotted_line_divider),
//                contentDescription = null,
//                Modifier
//                    .align(alignment = Alignment.CenterHorizontally)
//                    .padding(vertical = 3.dp),
//                contentScale = ContentScale.FillHeight,
//            )
        }
        Column {
            Text(
                text = description,
                Modifier.padding(top = 1.dp),
                style = Typography.bodyMedium
            )
        }
    }
}

val exerciseStub = Exercise(
    id = 0,
    title = "Сгибание на бицепс стоя",
    description = "Данное упражнение является самым популярным упражнением на мышцы бицепса. Упражнения изолирующее. Может выполняться как со штангой, так и с гантелями. Существует разновидность упражнения, когда вместо обычно штанги используется штанга с изогнутым грифом (более эффективный и безопасный способ).",
    MuscleGroup.CHEST,
    EquipmentType.BARBELL,
    listOf(
        "Встаньте прямо, предварительно взяв в руку штангу. Руки должны находится на уровне плеч. Хват снизу. Локти находятся близко к телу. Это будет вашей стартовой позицией",
        "Держа верхние отделы рук в неподвижном состоянии, выдохните и согните руки в локтях усилиями бицепса. В данном движении участвуют только нижние отделы руки (предплечье)",
        "Продолжайте упражнения до того положения, когда ваши руки будут находится на уровне плеч. В верхней точке задержитесь на несколько секунд и максимально напрягите бицепс",
        "Вдохните и медленно начните опускать штангу в стартовое положение",
        "Выполните движение необходимо количество раз"
    ),
    "Вы можете попробовать выполнение данного упражнения узким хватом.")