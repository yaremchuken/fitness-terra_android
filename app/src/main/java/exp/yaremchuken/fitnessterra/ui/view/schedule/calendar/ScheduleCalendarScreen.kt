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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import exp.yaremchuken.fitnessterra.AppSettings
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.Schedule
import exp.yaremchuken.fitnessterra.toLocalDate
import exp.yaremchuken.fitnessterra.ui.route.Screen
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.ui.view.workout.workoutStub
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_DATE
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit

private const val DAYS_IN_WEEK = 7

// Calendar should show 6 nearest weeks
private const val WEEKS_IN_CALENDAR = 6

private val MONTH_FORMATTER = DateTimeFormatter.ofPattern("MMM yyyy")

@Preview
@Composable
fun ScheduleCalendarScreen(
    navController: NavController = NavController(LocalContext.current),
    schedules: List<Schedule> = schedulesStub
) {
    var yearMonth by remember { mutableStateOf(YearMonth.now()) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val calendarDateWidth = ((screenWidth / DAYS_IN_WEEK).value - 8).dp

    val firstDay = yearMonth.atDay(1)
    val firstDayOffset = -firstDay.dayOfWeek.value + 1
    val dates: MutableList<LocalDate> = mutableListOf()
    for (i in firstDayOffset until (DAYS_IN_WEEK * WEEKS_IN_CALENDAR - firstDayOffset)) {
        dates.add(firstDay.plusDays(i.toLong()))
    }

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
                    onClick = { yearMonth = yearMonth.minusMonths(1) },
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
                                onClick = { navController
                                    .navigate("${Screen.SCHEDULE_DATE_SCREEN.name}/${onDate.format(ISO_DATE)}") },
                                width = calendarDateWidth,
                                date = onDate,
                                month = yearMonth,
                                scheduled = schedules.filter { schedule ->
                                    schedule.scheduledAt.toLocalDate() == onDate
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

val schedulesStub =
    listOf(
        Schedule(
            Instant.now().minus(3, ChronoUnit.DAYS),
            workoutStub,
            true
        ),
        Schedule(
            Instant.now(),
            workoutStub
        ),
        Schedule(
            Instant.now().plus(3, ChronoUnit.HOURS).plus(20, ChronoUnit.MINUTES),
            workoutStub
        ),
        Schedule(
            Instant.now().plus(1, ChronoUnit.DAYS),
            workoutStub
        ),
        Schedule(
            Instant.now().minus(1, ChronoUnit.DAYS),
            workoutStub
        ),
    )
