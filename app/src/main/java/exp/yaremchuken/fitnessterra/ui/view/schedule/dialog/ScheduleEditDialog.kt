package exp.yaremchuken.fitnessterra.ui.view.schedule.dialog

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.AppSettings
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.Workout
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.theme.AppColor
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.ui.view.schedule.date.ScheduleTemplate
import exp.yaremchuken.fitnessterra.util.Utils
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun ScheduleEditDialog(
    onApprove: (template: ScheduleTemplate) -> Unit,
    onCancel: (template: ScheduleTemplate) -> Unit,
    onInfo: (workoutId: Long) -> Unit,
    existedWorkouts: List<Workout>,
    template: ScheduleTemplate
) {
    val scrollState = rememberScrollState()

    var chosenWorkout by remember { mutableStateOf(template.workout) }
    var cancelPressed by remember { mutableStateOf(false) }
    val weekdays = remember { mutableStateListOf<DayOfWeek>() }

    weekdays.addAll(template.weekdays)

    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    var firstWeekday: LocalDate = LocalDate.now()
    firstWeekday = firstWeekday.minusDays(firstWeekday.dayOfWeek.value.toLong()-1)

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
                    .padding(top = 6.dp, bottom = 6.dp, start = 6.dp)
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
                text = Utils.TIME_FORMAT.format(template.scheduledAt),
                Modifier.padding(bottom = 2.dp),
                style = Typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Divider(Modifier.padding(bottom = 6.dp))
            Text(
                text = stringResource(R.string.repeat_every_title),
                Modifier.fillMaxWidth(),
                style = Typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                DayOfWeek.entries.forEach {
                    Box(
                        Modifier
                            .weight(1F)
                            .padding(horizontal = 2.dp)
                            .clickable { if (weekdays.contains(it)) weekdays.remove(it) else weekdays.add(it) }
                            .background(
                                color = if (weekdays.contains(it)) Color.Green else Color.Transparent,
                                shape = UIConstants.ROUNDED_CORNER
                            )
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                UIConstants.ROUNDED_CORNER
                            )
                    ) {
                        Text(
                            text = firstWeekday
                                .plusDays((it.value - 1).toLong())
                                .dayOfWeek
                                .getDisplayName(TextStyle.SHORT, AppSettings.locale())
                                .lowercase(),
                            Modifier
                                .align(Alignment.Center)
                                .padding(vertical = 8.dp),
                            style = Typography.bodyLarge
                        )
                    }
                }
            }
            Divider(Modifier.padding(bottom = 6.dp))
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
                        { onBackPressedDispatcher?.onBackPressed(); onInfo(workout.id) },
                        selected = chosenWorkout == workout,
                        workout = workout
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
                        onClick = { onCancel(template); onBackPressedDispatcher?.onBackPressed() },
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
            if (!template.isExists) {
                Button(
                    onClick = {
                                onApprove(
                                    ScheduleTemplate(template.scheduledAt)
                                        .withWorkout(chosenWorkout!!)
                                        .withWeekdays(weekdays)
                                );
                                onBackPressedDispatcher?.onBackPressed()
                              },
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 12.dp),
                    shape = UIConstants.ROUNDED_CORNER
                ) {
                    Text(
                        text = stringResource(id = R.string.schedule_btn_title),
                        style = Typography.titleLarge
                    )
                }
            } else {
                if (template.workout?.id == chosenWorkout?.id && same(weekdays, template.weekdays)) {
                    Button(
                        onClick = { cancelPressed = true },
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp, horizontal = 12.dp),
                        shape = UIConstants.ROUNDED_CORNER,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text(
                            text = stringResource(id = R.string.cancel_schedule_btn_title),
                            style = Typography.titleLarge
                        )
                    }
                } else {
                    Button(
                        onClick = {
                                    onApprove(
                                        ScheduleTemplate(template.scheduledAt)
                                            .withWorkout(chosenWorkout!!)
                                            .withWeekdays(weekdays)
                                    );
                                    onBackPressedDispatcher?.onBackPressed()
                                  },
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp, horizontal = 12.dp),
                        shape = UIConstants.ROUNDED_CORNER
                    ) {
                        Text(
                            text = stringResource(id = R.string.schedule_btn_title),
                            style = Typography.titleLarge
                        )
                    }
                }
            }
        }
    }
}

private fun same(a: List<DayOfWeek>, b: List<DayOfWeek>): Boolean {
    if (a.size != b.size) {
        return false
    }
    for(w in a) {
        if (!b.contains(w)) {
            return false
        }
    }
    return true
}