package exp.yaremchuken.fitnessterra.ui.view.schedule.calendar

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import exp.yaremchuken.fitnessterra.AppSettings
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.History
import exp.yaremchuken.fitnessterra.data.model.Schedule
import exp.yaremchuken.fitnessterra.data.model.TimedWorkout
import exp.yaremchuken.fitnessterra.toLocalDate
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.viewmodel.ScheduleCalendarViewModel
import exp.yaremchuken.fitnessterra.viewmodel.ScheduleCalendarViewModel.Companion.DAYS_IN_WEEK
import exp.yaremchuken.fitnessterra.viewmodel.ScheduleCalendarViewModel.Companion.WEEKS_IN_CALENDAR
import exp.yaremchuken.fitnessterra.viewmodel.WorkoutSequenceHelper
import exp.yaremchuken.fitnessterra.viewmodel.WorkoutSequenceHelper.SEQUENCE_DEFAULT_PERIOD_FOR_DISPLAY
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle

val MONTH_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM yyyy")

@Composable
fun ScheduleCalendarScreen(
    gotoCalendarDate: (date: LocalDate) -> Unit,
    viewModel: ScheduleCalendarViewModel = hiltViewModel()
) {
    var yearMonth by remember { mutableStateOf(YearMonth.now()) }
    val schedules = remember { mutableStateListOf<Schedule>() }
    val sequences = remember { mutableStateMapOf<LocalDate, List<TimedWorkout>>() }
    val histories = remember { mutableStateListOf<History>() }

    val dates = viewModel.getDatesForMonth(yearMonth)

    LaunchedEffect(Unit) {
        viewModel.getSchedules(dates[0], dates[dates.size-1]).collect { sch ->
            schedules.clear()
            schedules.addAll(
                sch.map { e -> viewModel.fromEntity(e) }
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getHistories(dates[0], dates[dates.size-1]).collect { his ->
            histories.clear()
            histories.addAll(
                his.map { e -> viewModel.fromEntity(e) }
            )

            viewModel.getSequences().collect { seqs ->
                sequences.clear()
                if (dates.first().isBefore(LocalDate.now().plusDays(SEQUENCE_DEFAULT_PERIOD_FOR_DISPLAY)) &&
                    dates.last().isAfter(LocalDate.now().minusDays(1))
                ){
                    sequences.putAll(
                        WorkoutSequenceHelper.getTimedWorkoutsFromToday(
                            SEQUENCE_DEFAULT_PERIOD_FOR_DISPLAY,
                            seqs.map { e -> viewModel.fromEntity(e) },
                            histories
                        )
                    )
                }
            }
        }
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val calendarDateWidth = ((screenWidth / DAYS_IN_WEEK).value - 8).dp

    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
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
                text = stringResource(R.string.workout_calendar),
                Modifier.align(Alignment.Center),
                style = Typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        }
        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { yearMonth = yearMonth.minusMonths(1) },
                    Modifier
                        .padding(all = 6.dp)
                        .width(30.dp)
                        .height(30.dp)
                ) {
                    Image(painter = painterResource(id = R.drawable.ic_backward_shallow), contentDescription = null)
                }
                Text(
                    text = yearMonth.format(MONTH_FORMATTER),
                    Modifier.align(Alignment.CenterVertically),
                    style = Typography.bodyLarge
                )
                IconButton(
                    onClick = { yearMonth = yearMonth.plusMonths(1) },
                    Modifier
                        .padding(all = 6.dp)
                        .width(30.dp)
                        .height(30.dp)
                ) {
                    Image(painter = painterResource(id = R.drawable.ic_forward_shallow), contentDescription = null)
                }
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (i in 0 until DAYS_IN_WEEK) {
                    val dayOfWeek = dates[i].dayOfWeek
                    val isWeekend = dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY
                    Text(
                        text = dayOfWeek.getDisplayName(TextStyle.SHORT, AppSettings.locale()),
                        Modifier.width(calendarDateWidth),
                        textAlign = TextAlign.Center,
                        color = if (isWeekend) Color.Red else Color.Black
                    )
                }
            }
            Column(
                Modifier.padding(bottom = 20.dp)
            ) {
                for (i in 0 until WEEKS_IN_CALENDAR) {
                    Row(
                        Modifier
                            .weight(1F)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (j in 0 until DAYS_IN_WEEK) {
                            val onDate = dates[i * DAYS_IN_WEEK + j]
                            ScheduleCalendarDateView(
                                onClick = { gotoCalendarDate(onDate) },
                                width = calendarDateWidth,
                                date = onDate,
                                month = yearMonth,
                                scheduled = viewModel.getWorkoutsForDate(onDate, schedules, sequences[onDate]),
                                histories = histories.filter { history ->
                                    history.startedAt.toLocalDate() == onDate
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
