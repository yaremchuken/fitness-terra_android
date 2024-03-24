package exp.yaremchuken.fitnessterra.ui.view.schedule.date

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import exp.yaremchuken.fitnessterra.AppSettings
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.History
import exp.yaremchuken.fitnessterra.data.model.Schedule
import exp.yaremchuken.fitnessterra.data.model.TimedWorkout
import exp.yaremchuken.fitnessterra.data.model.Workout
import exp.yaremchuken.fitnessterra.toInstant
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.ui.view.schedule.dialog.ScheduleEditDialog
import exp.yaremchuken.fitnessterra.util.Utils
import exp.yaremchuken.fitnessterra.viewmodel.ScheduleDateViewModel
import exp.yaremchuken.fitnessterra.viewmodel.WorkoutSequenceHelper
import exp.yaremchuken.fitnessterra.viewmodel.WorkoutSequenceHelper.SEQUENCE_DEFAULT_PERIOD_FOR_DISPLAY
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

val REFRESH_TIMER_DELAY: Duration = 5.seconds

@Composable
fun ScheduleDateScreen(
    gotoWorkout: (workoutId: Long) -> Unit,
    gotoHistory: (startedAt: Instant) -> Unit,
    date: LocalDate,
    viewModel: ScheduleDateViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    var editedSchedule by remember { mutableStateOf<ScheduleTemplate?>(null) }

    val schedules = remember { mutableStateListOf<Schedule>() }
    val sequenced = remember { mutableStateListOf<TimedWorkout>() }
    val histories = remember { mutableStateListOf<History>() }
    val existedWorkouts = remember { mutableStateListOf<Workout>() }

    var timelineOffset by remember { mutableIntStateOf(0) }

    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val dateFormat =
        "${date.dayOfWeek.getDisplayName(TextStyle.SHORT, AppSettings.locale())}, ${date.format(Utils.DATE_FORMAT)}"

    LaunchedEffect(Unit) {
        viewModel.getWorkouts().collect { works ->
            existedWorkouts.clear()
            existedWorkouts.addAll(
                works.map { w -> viewModel.fromEntity(w) }
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getSchedules(date).collect { sch ->
            schedules.clear()
            schedules.addAll(
                sch.map { e -> viewModel.fromEntity(e) }
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getHistories(date).collect { his ->
            histories.clear()
            histories.addAll(
                his.map { e -> viewModel.fromEntity(e) }
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getSequences().collect { seq ->
            sequenced.clear()
            val sequence = seq
                .map { s -> viewModel.fromEntity(s) }
                .filter { s -> s.weekdays.contains(date.dayOfWeek)}
            val workouts = WorkoutSequenceHelper
                .getTimedWorkoutsFromToday(SEQUENCE_DEFAULT_PERIOD_FOR_DISPLAY, sequence, histories)
            sequenced.addAll(workouts[date].orEmpty())
        }
    }

    // Refresher for current time red line.
    LaunchedEffect(Unit) {
        do {
            val now = Instant.now().atZone(ZoneId.systemDefault())
            val hourOffset = (now.hour * HOUR_BLOCK_HEIGHT.value).toInt()
            val minuteOffset = now.minute * (HOUR_BLOCK_HEIGHT.value / 60.0).toInt()

            timelineOffset = hourOffset + minuteOffset

            delay(REFRESH_TIMER_DELAY)
        } while (date == LocalDate.now())
    }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    LaunchedEffect(Unit) {
        scope.launch { scrollState.scrollTo(timelineOffset + screenHeight/2) }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        if (editedSchedule != null) {
            Dialog(onDismissRequest = { editedSchedule = null }) {
                ScheduleEditDialog(
                    onApprove = { viewModel.insertSchedule(it.toSchedule()) },
                    onCancel = { viewModel.deleteSchedule(it.toSchedule()); editedSchedule = null },
                    onInfo = { gotoWorkout(it) },
                    existedWorkouts = existedWorkouts,
                    template = editedSchedule!!
                )
            }
        }
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
                text = dateFormat,
                Modifier.align(Alignment.Center),
                style = Typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        }
        Box(
            Modifier.verticalScroll(scrollState)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                for (i in 0..23) {
                    HourBlockEntry(
                        { editedSchedule = viewModel.getTemplate(schedules, date, i) },
                        i
                    )
                }
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(start = 64.dp, end = 8.dp)
            ) {
                if (date >= LocalDate.now()) {
                    schedules.forEach {
                        ScheduledWorkoutBlockView(
                            onClick = { editedSchedule = ScheduleTemplate.toTemplate(it) },
                            scheduledAt = it.scheduledAt,
                            workout = it.workout,
                            isEditable = true
                        )
                    }
                    sequenced.forEach {
                        ScheduledWorkoutBlockView(
                            onClick = { /* nothing */ },
                            scheduledAt = it.scheduledAt.atDate(date).toInstant(),
                            workout = it.workout
                        )
                    }
                }
                histories.forEach {
                    ScheduledWorkoutBlockView(
                        onClick = { gotoHistory(it.startedAt) },
                        scheduledAt = it.startedAt,
                        workout = it.workout,
                        isHistory = true
                    )
                }
            }
            if (date == LocalDate.now()) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                ) {
                    Row(
                        Modifier.offset(y = timelineOffset.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(painter = painterResource(id = R.drawable.ic_red_dot), contentDescription = null)
                        Divider(
                            thickness = 2.dp,
                            color = Color.Red
                        )
                    }
                }
            }
        }
    }
}