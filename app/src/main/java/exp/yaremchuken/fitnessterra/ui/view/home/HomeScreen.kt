package exp.yaremchuken.fitnessterra.ui.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import exp.yaremchuken.fitnessterra.data.model.History
import exp.yaremchuken.fitnessterra.data.model.Schedule
import exp.yaremchuken.fitnessterra.data.model.TimedWorkout
import exp.yaremchuken.fitnessterra.toInstant
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.viewmodel.HomeViewModel
import exp.yaremchuken.fitnessterra.viewmodel.WorkoutSequenceHelper
import java.time.Instant
import java.time.LocalDate

@Composable
fun HomeScreen(
    gotoCalendar: () -> Unit,
    gotoSequencer: () -> Unit,
    gotoExerciseLibrary: () -> Unit,
    gotoWorkoutLibrary: () -> Unit,
    gotoWorkout: (workoutId: Long) -> Unit,
    gotoHistory: (finishedAt: Instant) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val todaySchedules = remember { mutableStateListOf<Schedule>() }
    val todaySequenced = remember { mutableStateListOf<TimedWorkout>() }
    val latestHistory = remember { mutableStateListOf<History>() }

    LaunchedEffect(Unit) {
        viewModel.getTodaySchedules().collect { schedules ->
            todaySchedules.clear()
            todaySchedules.addAll(
                schedules.map { e -> viewModel.fromEntity(e) }
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getLatestHistory(31).collect { his ->
            latestHistory.clear()
            latestHistory.addAll(
                his.map { e -> viewModel.fromEntity(e) }
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getSequences().collect { seq ->
            todaySequenced.clear()
            val sequence = seq
                .map { s -> viewModel.fromEntity(s) }
                .filter { s -> s.weekdays.contains(LocalDate.now().dayOfWeek)}
            val workouts = WorkoutSequenceHelper.getTimedWorkoutsFromToday(0, sequence, latestHistory)
            todaySequenced.addAll(workouts[LocalDate.now()].orEmpty())
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            Modifier
                .padding(all = 12.dp)
                .verticalScroll(scrollState)
        ) {
            if (todaySchedules.isNotEmpty() || todaySequenced.isNotEmpty()) {
                val notCompleted = viewModel.getNotCompleted(latestHistory, todaySchedules, todaySequenced)
                if (notCompleted.isEmpty()) {
                    Text(
                        text = stringResource(R.string.today_workouts_completed_title),
                        Modifier.padding(bottom = 12.dp),
                        style = Typography.titleLarge
                    )
                } else {
                    Text(
                        text = stringResource(R.string.scheduled_for_today_title),
                        Modifier.padding(bottom = 12.dp),
                        style = Typography.titleLarge
                    )
                    notCompleted.forEach {
                        ScheduledWorkoutPreviewBlock(
                            onClick = { gotoWorkout(it.workout.id) },
                            scheduledAt = it.scheduledAt.atDate(LocalDate.now()).toInstant(),
                            workout = it.workout
                        )
                    }
                }
            } else {
                Text(
                    text = stringResource(R.string.no_schedules_today_title),
                    Modifier.padding(bottom = 12.dp),
                    style = Typography.titleLarge
                )
            }

            Divider()
            Column(
                Modifier.padding(vertical = 12.dp)
            ) {
                LinkHorizontalBlock(
                    onClick = { gotoCalendar() },
                    isScheduleLink = true
                )
            }

            Divider()
            Column(
                Modifier.padding(vertical = 12.dp)
            ) {
                LinkHorizontalBlock(
                    onClick = { gotoSequencer() }
                )
            }

            Divider()
            Row {
                Column(
                    Modifier
                        .weight(1F)
                        .padding(vertical = 12.dp)
                ) {
                    LinkVerticalBlock(
                        onClick = { gotoExerciseLibrary() },
                        isExerciseLink = true
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(
                    Modifier
                        .weight(1F)
                        .padding(vertical = 12.dp)
                ) {
                    LinkVerticalBlock(
                        onClick = { gotoWorkoutLibrary() }
                    )
                }
            }

            if (latestHistory.isNotEmpty()) {
                Divider()
                Text(
                    text = stringResource(R.string.recently_completed_workouts_title),
                    Modifier.padding(vertical = 12.dp),
                    style = Typography.titleLarge
                )
            }
            latestHistory.take(7).forEach {
                ScheduledWorkoutPreviewBlock(
                    { gotoHistory(it.finishedAt) },
                    it.startedAt,
                    it.workout,
                    true
                )
            }
        }
    }
}

@Composable
fun LinkHorizontalBlock(
    onClick: () -> Unit,
    isScheduleLink: Boolean = false
) {
    Row(
        Modifier
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = Color.LightGray,
                UIConstants.ROUNDED_CORNER
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            Modifier
                .weight(1F)
                .padding(all = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = if (isScheduleLink) R.drawable.ic_calendar else R.drawable.ic_sequence),
                contentDescription = null,
                Modifier
                    .width(48.dp)
                    .height(48.dp)
            )
            Text(
                text = stringResource(
                    if (isScheduleLink) R.string.to_workout_schedules_link
                    else R.string.to_workout_sequences_link
                ),
                Modifier.padding(start = 6.dp),
                style = Typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        IconButton(
            onClick = { onClick() },
            Modifier
                .padding(end = 20.dp)
                .height(36.dp)
                .width(36.dp)
                .align(Alignment.CenterVertically)
        ) {
            Image(painter = painterResource(id = R.drawable.ic_forward_filled), contentDescription = null)
        }
    }
}

@Composable
fun LinkVerticalBlock(
    onClick: () -> Unit,
    isExerciseLink: Boolean = false
) {
    val linkText =
        if (isExerciseLink) {
            stringResource(R.string.to_exercise_library_link)
        } else {
            stringResource(R.string.to_workout_library_link)
        }

    Column(
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = Color.LightGray,
                UIConstants.ROUNDED_CORNER
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .padding(all = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = if (isExerciseLink) R.drawable.ic_exercise else R.drawable.ic_workout),
                contentDescription = null,
                Modifier
                    .width(56.dp)
                    .height(56.dp)
            )
            Text(
                text = linkText,
                style = Typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
