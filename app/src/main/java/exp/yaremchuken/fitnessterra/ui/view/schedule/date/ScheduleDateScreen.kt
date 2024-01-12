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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import exp.yaremchuken.fitnessterra.AppSettings
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.Schedule
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.ui.view.schedule.dialog.ScheduleEditDialog
import exp.yaremchuken.fitnessterra.util.Utils
import exp.yaremchuken.fitnessterra.viewmodel.ScheduleDateViewModel
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

val REFRESH_TIMER_DELAY: Duration = 5.seconds

@Composable
fun ScheduleDateScreen(
    onWorkoutDetailsClick: (id: Long) -> Unit,
    date: LocalDate,
    viewModel: ScheduleDateViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()

    var editedSchedule by remember { mutableStateOf<ScheduleTemplate?>(null) }

    val todaySchedules = remember { mutableStateListOf<Schedule>() }

    var timelineOffset by remember { mutableIntStateOf(0) }

    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val dateFormat =
        "${date.dayOfWeek.getDisplayName(TextStyle.SHORT, AppSettings.locale())}, ${date.format(Utils.DATE_FORMAT)}"

    val existedWorkouts = viewModel.getWorkouts()

    LaunchedEffect(Unit) {
        todaySchedules.clear()
        viewModel.getSchedules(date).collect { schedules ->
            todaySchedules.addAll(
                schedules.map { e -> viewModel.fromEntity(e) }
            )
        }
        viewModel.getSchedules(listOf(date.dayOfWeek)).collect { schedules ->
            todaySchedules.addAll(
                schedules.map { e -> viewModel.fromEntity(e) }
            )
        }
    }

    // Refresher for current time red line.
    LaunchedEffect(Unit) {
        while (true) {
            val now = Instant.now().atZone(ZoneId.systemDefault())
            val hourOffset = (now.hour * HOUR_BLOCK_HEIGHT.value).toInt()
            val minuteOffset = now.minute * (HOUR_BLOCK_HEIGHT.value / 60.0).toInt()

            timelineOffset = hourOffset + minuteOffset

            delay(REFRESH_TIMER_DELAY)
        }
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
                        { editedSchedule = viewModel.getTemplate(todaySchedules, date, i) },
                        i
                    )
                }
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(start = 64.dp, end = 8.dp)
            ) {
                todaySchedules.forEach {
                    ScheduledWorkoutBlockView(
                        { editedSchedule = ScheduleTemplate.toTemplate(it) },
                        it.scheduledAt,
                        it.workout
                    )
                }
            }
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