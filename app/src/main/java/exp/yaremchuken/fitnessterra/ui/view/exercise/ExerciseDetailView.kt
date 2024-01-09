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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.bitmap
import exp.yaremchuken.fitnessterra.data.model.EquipmentType
import exp.yaremchuken.fitnessterra.data.model.Exercise
import exp.yaremchuken.fitnessterra.data.model.MuscleGroupType
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.ui.view.animation.ExerciseAnimation
import exp.yaremchuken.fitnessterra.uppercaseFirstChar
import java.util.Locale

@Preview
@Composable
fun ExerciseDetailView(
    exercise: Exercise = exerciseBackStub
) {
    val scrollState = rememberScrollState()

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
            ExerciseAnimation(
                exercise = exercise,
                modifier = Modifier.align(Alignment.Center),
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
        Text(
            text = exercise.titleLocalized(),
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
                text = exercise.descriptionLocalized(),
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
                                    style = Typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = MuscleGroupType.i18n(exercise.muscleGroup!!).uppercaseFirstChar(),
                                    style = Typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.padding(top = 8.dp))
                                Text(
                                    text = stringResource(id = R.string.involved_muscles_title),
                                    style = Typography.titleMedium,
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
                                style = Typography.titleMedium,
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
                style = Typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Column {
                exercise.stepsLocalized().forEachIndexed { idx, it ->
                    ExerciseStepView(idx+1, it)
                }
            }
            Spacer(modifier = Modifier.padding(bottom = 8.dp))
            if (exercise.advisesLocalized().isNotEmpty()) {
                Divider()
                Column(
                    Modifier.padding(vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.advises),
                        Modifier.padding(bottom = 12.dp),
                        style = Typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    exercise.advisesLocalized().forEach {
                        Row(
                            Modifier.padding(bottom = 8.dp)
                        ) {
                            Text(
                                text = it,
                                style = Typography.bodyMedium
                            )
                        }
                    }
                }
            }
            if (exercise.warningsLocalized().isNotEmpty()) {
                Divider()
                Text(
                    text = stringResource(id = R.string.safety_precautions_title),
                    Modifier.padding(top = 8.dp, bottom = 12.dp),
                    style = Typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                exercise.warningsLocalized().forEach {
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

val exerciseBicepsStub = Exercise(
    id = 0,
    title = mapOf(
        Pair(
            Locale.forLanguageTag("ru"),
            "Сгибание на бицепс стоя"
        ),
        Pair(
            Locale.ENGLISH,
            "Flexion on the biceps while standing"
        )
    ),
    description = mapOf(
        Pair(
            Locale.forLanguageTag("ru"),
            "Данное упражнение является самым популярным упражнением на мышцы бицепса. Упражнения изолирующее. Может выполняться как со штангой, так и с гантелями. Существует разновидность упражнения, когда вместо обычно штанги используется штанга с изогнутым грифом (более эффективный и безопасный способ)."
        ),
        Pair(
            Locale.ENGLISH,
            "This exercise is the most popular exercise for biceps muscles. The exercises are isolating. It can be performed with both a barbell and dumbbells. There is a type of exercise where a barbell with a curved neck is used instead of a barbell (a more effective and safer way)."
        )
    ),
    muscleGroup = MuscleGroupType.BICEPS,
    muscles = listOf(MuscleGroupType.FOREARMS),
    equipment = EquipmentType.BARBELL,
    steps = mapOf(
        Pair(
            Locale.forLanguageTag("ru"),
            listOf(
                "Встаньте прямо, предварительно взяв в руку штангу. Руки должны находится на уровне плеч. Хват снизу. Локти находятся близко к телу. Это будет вашей стартовой позицией",
                "Держа верхние отделы рук в неподвижном состоянии, выдохните и согните руки в локтях усилиями бицепса. В данном движении участвуют только нижние отделы руки (предплечье)",
                "Продолжайте упражнения до того положения, когда ваши руки будут находится на уровне плеч. В верхней точке задержитесь на несколько секунд и максимально напрягите бицепс",
                "Вдохните и медленно начните опускать штангу в стартовое положение",
                "Выполните движение необходимо количество раз"
            )
        ),
        Pair(
            Locale.ENGLISH,
            listOf(
                "Stand up straight, first taking a barbell in your hand. The arms should be at shoulder level. Grip from below. The elbows are close to the body. This will be your starting position",
                "Keeping the upper arms stationary, exhale and bend your arms at the elbows with biceps efforts. Only the lower arm (forearm) is involved in this movement",
                "Continue the exercises until your arms are at shoulder level. At the top point, hold for a few seconds and tighten your biceps as much as possible",
                "Inhale and slowly start lowering the barbell to the starting position",
                "Perform the movement the required number of times"
            )
        )
    ),
    advises = mapOf(
        Pair(
            Locale.forLanguageTag("ru"),
            listOf(
                "Вы можете попробовать выполнение данного упражнения узким хватом"
            )
        ),
        Pair(
            Locale.ENGLISH,
            listOf(
                "You can try doing this exercise with a narrow grip"
            )
        )
    )
)

val exerciseBackStub = Exercise(
    id = 1,
    title = mapOf(
        Pair(
            Locale.forLanguageTag("ru"),
            "Тяга гантелей в наклоне"
        ),
        Pair(
            Locale.ENGLISH,
            "Lifting dumbbells in a tilt"
        )
    ),
    description = mapOf(
        Pair(
            Locale.forLanguageTag("ru"),
            "Упражнение на среднюю область спины."
        ),
        Pair(
            Locale.ENGLISH,
            "Exercise on the middle back area."
        )
    ),
    muscleGroup = MuscleGroupType.BACK,
    muscles = listOf(MuscleGroupType.LATISSIMUS_DORSI, MuscleGroupType.DELTOIDS, MuscleGroupType.BICEPS),
    equipment = EquipmentType.BARBELL,
    steps = mapOf(
        Pair(
            Locale.forLanguageTag("ru"),
            listOf(
                "Возьмите в обе руки гантели, ладони должны быть обращены к туловищу. Слегка согните колени и наклоните туловище вперед, сгибаясь в пояснице до тех пор, пока спина не будет практически параллельна полу, при этом держите спину прямо. Следите за тем, чтобы голова была поднята вверх. Руки должны свисать перпендикулярно полу и туловищу. Это Ваше исходное положение",
                "Находясь в таком положении, на выдохе притяните гантели к себе, локти должны быть прижаты к телу (частью руки от локтя до кисти не делайте никаких усилий, кроме как просто удерживайте вес). На пике этого движения, напрягите мышцы спины и на секунду зафиксируйте тело в таком положении",
                "На вдохе медленно опустите вес в исходную позицию",
                "Выполните необходимое количество повторений"
            )
        ),
        Pair(
            Locale.ENGLISH,
            listOf(
                "Take dumbbells in both hands, palms should be facing the torso. Bend your knees slightly and tilt your torso forward, bending at the waist until your back is almost parallel to the floor, while keeping your back straight. Make sure that the head is raised up. The arms should hang perpendicular to the floor and the torso. This is your starting position",
                "While in this position, pull the dumbbells towards you on exhalation, the elbows should be pressed against the body (do not make any effort with the part of the arm from the elbow to the hand, except just hold the weight). At the peak of this movement, tighten your back muscles and fix your body in this position for a second",
                "As you inhale, slowly lower the weight to the starting position",
                "Perform the required number of repetitions"
            )
        )
    ),
    advises = mapOf(
        Pair(
            Locale.forLanguageTag("ru"),
            listOf(
                "Вы можете выполнять это упражнение, используя вместо гантелей трос нижнего блока с V- образной рукоятью или штангу. Также можно делать это упражнение с использованием супинированного или нейтрального хвата"
            )
        ),
        Pair(
            Locale.ENGLISH,
            listOf(
                "You can perform this exercise using a V-shaped lower block cable or a barbell instead of dumbbells. You can also do this exercise using a supinated or neutral grip"
            )
        )
    ),
    warnings = mapOf(
        Pair(
            Locale.forLanguageTag("ru"),
            listOf(
                "Это упражнение не рекомендуется выполнять людям, у которых есть проблемы со спиной. В данном случае лучше выбрать упражнение Тяга на низком блоке",
                "Ни в коем случае не прогибайте спину, т.к. это может привести к травме спины",
                "Будьте внимательны в выборе веса для упражнения. Если сомневаетесь, то лучше выберите меньший вес, чем больший"
            )
        ),
        Pair(
            Locale.ENGLISH,
            listOf(
                "This exercise is not recommended for people who have back problems. In this case, it is better to choose the Low-block Traction exercise",
                "In any case, do not bend your back, because this can lead to back injury",
                "Be careful in choosing the weight for the exercise. If in doubt, it is better to choose a smaller weight than a larger one"
            )
        )
    )
)
