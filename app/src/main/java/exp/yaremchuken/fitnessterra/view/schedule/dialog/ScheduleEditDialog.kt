package exp.yaremchuken.fitnessterra.view.schedule.dialog

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.model.Schedule
import exp.yaremchuken.fitnessterra.model.Workout
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.theme.AppColor
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.utils.Utils
import exp.yaremchuken.fitnessterra.view.schedule.schedulesStub
import exp.yaremchuken.fitnessterra.view.workout.workoutStub
import java.time.Instant

@Preview
@Composable
fun ScheduleEditDialog(
    onSchedule: (workout: Workout?) -> Unit = {},
    onCancel: () -> Unit = {},
    existedWorkouts: List<Workout> = listOf(workoutStub, workoutStub),
    time: Instant? = null,
    schedule: Schedule? = schedulesStub[0]
) {
    val scrollState = rememberScrollState()

    var chosenWorkout by remember { mutableStateOf<Workout?>(null) }
    var cancelPressed by remember { mutableStateOf(false) }

    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Column(
        Modifier
            .background(
                color = AppColor.LightestGray,
                shape = UIConstants.ROUNDED_CORNER
            )
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(
                    color = Color.Gray,
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                )
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
                text = stringResource(R.string.schedule_workout),
                Modifier.align(Alignment.Center),
                style = Typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        }
        Column(
            Modifier
                .fillMaxWidth()
                .padding(all = 12.dp)
        ) {
            Text(
                text = Utils.TIME_FORMAT.format(time ?: schedule?.scheduledAt ?: Instant.EPOCH),
                Modifier.padding(bottom = 2.dp),
                style = Typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Divider(Modifier.padding(bottom = 6.dp))
            if (schedule == null) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                ) {
                    existedWorkouts.forEachIndexed { index, workout ->
                        if (index != 0) {
                            Spacer(Modifier.padding(vertical = 2.dp))
                        }
                        WorkoutSelectableRowView(
                            { chosenWorkout = workout },
                            selected = chosenWorkout == workout,
                            workout = workout
                        )
                    }
                }
            } else {
                Text(
                    text = schedule.workout.title,
                    style = Typography.titleLarge
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        bitmap = Utils.getWorkoutPreview(LocalContext.current, schedule.workout),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
        }
        if (cancelPressed) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.cancel_workout_schedule_dialog),
                        style = Typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
                Row {
                    Button(
                        onClick = { cancelPressed = false },
                        Modifier
                            .weight(1F)
                            .padding(vertical = 10.dp, horizontal = 12.dp),
                        shape = UIConstants.ROUNDED_CORNER
                    ) {
                        Text(
                            text = stringResource(id = R.string.no_btn_title),
                            style = Typography.titleLarge
                        )
                    }
                    Button(
                        onClick = { onCancel() },
                        Modifier
                            .weight(1F)
                            .padding(vertical = 10.dp, horizontal = 12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        shape = UIConstants.ROUNDED_CORNER
                    ) {
                        Text(
                            text = stringResource(id = R.string.yes_btn_title),
                            style = Typography.titleLarge
                        )
                    }
                }
            }
        } else {
            Button(
                onClick = { if (schedule == null) onSchedule(chosenWorkout) else cancelPressed = true },
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 12.dp),
                shape = UIConstants.ROUNDED_CORNER,
                colors =
                    if (schedule == null) ButtonDefaults.buttonColors()
                    else ButtonDefaults.buttonColors(containerColor = Color.Red),
                enabled = schedule != null || chosenWorkout != null
            ) {
                Text(
                    text =
                    stringResource(id =
                    if (schedule == null) R.string.schedule_btn_title
                    else R.string.cancel_schedule_btn_title),
                    style = Typography.titleLarge
                )
            }
        }
    }
}
