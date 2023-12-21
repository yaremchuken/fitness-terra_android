package exp.yaremchuken.fitnessterra.view.schedule.day

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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.getHour
import exp.yaremchuken.fitnessterra.model.Schedule
import exp.yaremchuken.fitnessterra.toInstant
import exp.yaremchuken.fitnessterra.toLocalDate
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.utils.Utils
import exp.yaremchuken.fitnessterra.view.schedule.dialog.ScheduleEditDialog
import exp.yaremchuken.fitnessterra.view.schedule.schedulesStub
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

val REFRESH_TIMER_DELAY: Duration = 5.seconds

@Preview
@Composable
fun ScheduleDayView(
    date: LocalDate = LocalDate.now(),
    schedules: List<Schedule> = schedulesStub.filter { it.scheduledAt.toLocalDate() == LocalDate.now() }
) {
    val scrollState = rememberScrollState()
    var showEditorDialog by remember { mutableStateOf(false) }
    var editedHour by remember { mutableIntStateOf(-1) }
    var editedSchedule by remember { mutableStateOf<Schedule?>(null) }

    var timelineOffset by remember { mutableIntStateOf(0) }

    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val dateFormat =
        "${date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())}, ${date.format(Utils.DATE_FORMAT)}"

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
        if (showEditorDialog) {
            Dialog(onDismissRequest = { editedHour = -1; editedSchedule = null; showEditorDialog = false; }) {
                ScheduleEditDialog(
                    time = if (editedHour == -1) null else atHour(date, editedHour),
                    schedule = editedSchedule
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
                        {
                            if (getScheduledAtHour(schedules, i) == null) editedHour = i
                            else editedSchedule = getScheduledAtHour(schedules, i);
                            showEditorDialog = true
                        }, i)
                }
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(start = 64.dp, end = 8.dp)
            ) {
                schedules.forEach {
                    ScheduledWorkoutBlockView({ editedSchedule = it; showEditorDialog = true }, it)
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

private fun atHour(date: LocalDate, hour: Int) = date.toInstant().plus(hour.toLong(), ChronoUnit.HOURS)

/**
 * Checks if a workout is already scheduled for this time.
 */
private fun getScheduledAtHour(schedules: List<Schedule>, hour: Int): Schedule? =
    schedules.firstOrNull { it.scheduledAt.getHour() == hour }