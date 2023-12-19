package exp.yaremchuken.fitnessterra.view.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import exp.yaremchuken.fitnessterra.model.Schedule
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import java.time.LocalDate
import java.time.YearMonth


@Preview
@Composable
fun ScheduleCalendarDateView(
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
