package exp.yaremchuken.fitnessterra.ui.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    gotoCalendar: () -> Unit,
    gotoExerciseLibrary: () -> Unit,
    gotoWorkoutLibrary: () -> Unit,
    gotoWorkout: (workoutId: Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val todaySchedules = remember { mutableStateListOf<Schedule>() }
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
        viewModel.getLatestHistory(7).collect { his ->
            latestHistory.clear()
            latestHistory.addAll(
                his.map { e -> viewModel.fromEntity(e) }
            )
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
            if (todaySchedules.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.scheduled_for_today_title),
                    Modifier.padding(bottom = 12.dp),
                    style = Typography.titleLarge
                )
            } else {
                Text(
                    text = stringResource(R.string.no_schedules_today_title),
                    Modifier.padding(bottom = 12.dp),
                    style = Typography.titleLarge
                )
            }
            todaySchedules.forEach {
                ScheduledWorkoutPreviewBlock(
                    { gotoWorkout(it.workout.id) },
                    it.scheduledAt,
                    it.workout
                )
            }
            Divider()
            Column(
                Modifier.padding(vertical = 12.dp)
            ) {
                LinkBlock(
                    onClick = { gotoCalendar() },
                    isCalendarLink = true
                )
            }
            Divider()
            Column(
                Modifier.padding(vertical = 12.dp)
            ) {
                LinkBlock(
                    onClick = { gotoExerciseLibrary() },
                    isExerciseLink = true
                )
            }
            Divider()
            Column(
                Modifier.padding(vertical = 12.dp)
            ) {
                LinkBlock(
                    onClick = { gotoWorkoutLibrary() },
                    isWorkoutLink = true
                )
            }
            if (latestHistory.isNotEmpty()) {
                Divider()
                Text(
                    text = stringResource(R.string.recently_completed_workouts_title),
                    Modifier.padding(vertical = 12.dp),
                    style = Typography.titleLarge
                )
            }
            latestHistory.forEach {
                ScheduledWorkoutPreviewBlock(
                    { gotoWorkout(it.workout.id) },
                    it.startedAt,
                    it.workout,
                    true
                )
            }
        }
    }
}

@Composable
fun LinkBlock(
    onClick: () -> Unit,
    isCalendarLink: Boolean = false,
    isExerciseLink: Boolean = false,
    isWorkoutLink: Boolean = false
) {
    val linkText =
        if (isCalendarLink) {
            stringResource(R.string.to_workouts_schedule_link)
        } else if (isExerciseLink) {
            stringResource(R.string.to_exercise_library_link)
        } else if (isWorkoutLink) {
            stringResource(R.string.to_workout_library_link)
        } else {
            "EMPTY LINK!"
        }

    Row(
        Modifier
            .fillMaxWidth()
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
                painter =
                    if (isCalendarLink) painterResource(id = R.drawable.ic_calendar)
                    else painterResource(id = R.drawable.ic_library_icon),
                contentDescription = null,
                Modifier
                    .width(48.dp)
                    .height(48.dp)
            )
            Text(
                text = linkText,
                Modifier
                    .padding(start = 6.dp),
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
