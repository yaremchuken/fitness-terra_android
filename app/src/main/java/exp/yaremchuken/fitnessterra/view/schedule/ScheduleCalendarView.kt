package exp.yaremchuken.fitnessterra.view.schedule

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.model.Schedule
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.view.workout.workoutStub
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

private const val DAYS_IN_WEEK = 7

// Calendar should show 6 nearest weeks
private const val WEEKS_IN_CALENDAR = 6

private val MONTH_FORMATTER = DateTimeFormatter.ofPattern("MMM yyyy")

@Preview
@Composable
fun ScheduleCalendarView(
    schedules: List<Schedule> = listOf(scheduleStub)
) {
    var yearMonth by remember { mutableStateOf(YearMonth.now()) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val calendarDateWidth = ((screenWidth / DAYS_IN_WEEK).value - 8).dp

    val firstDay = YearMonth.now().atDay(1)
    val firstDayOffset = firstDay.dayOfWeek.value - 7
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
                text = "Workout Calendar",
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
                            val idx = i * DAYS_IN_WEEK + j
                            println(idx)
                            val onDate = dates[i * DAYS_IN_WEEK + j]
                            CalendarDateView(
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

@Preview
@Composable
fun CalendarDateView(
    width: Dp = 45.dp,
    date: LocalDate = LocalDate.now(),
    month: YearMonth = YearMonth.now(),
    scheduled: List<Schedule> = listOf(scheduleStub)
) {
    val isNotInMonth = date.isBefore(month.atDay(1)) || date.isAfter(month.atEndOfMonth())

    Column(
        Modifier
            .fillMaxHeight()
            .width(width + 6.dp)
            .padding(horizontal = 3.dp, vertical = 8.dp)
            .border(
                1.dp,
                if (isNotInMonth) Color.LightGray else Color.Gray,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                if (scheduled.isEmpty()) Color.White else Color.Green,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Box(
            Modifier
                .padding(end = 6.dp)
                .align(Alignment.End)
        ) {
            Text(
                text = "${date.dayOfMonth}".padStart(2, '0'),
                style = Typography.titleLarge,
                color = if (scheduled.isEmpty() && !isNotInMonth) Color.Black else Color.Gray
            )
        }
        if (scheduled.isNotEmpty()) {
            if (scheduled.size == 1) {
                Text(
                    text = scheduled[0].workout.title,
                    Modifier.padding(all = 2.dp)
                )
            } else {
                Text(
                    text = "${scheduled.size}",
                    Modifier
                        .padding(all = 2.dp)
                        .align(Alignment.CenterHorizontally),
                    style = Typography.titleLarge,
                    fontSize = 32.sp
                )
            }
        }
    }
}

val scheduleStub = Schedule(
    LocalDateTime.now(),
    workoutStub
)