package exp.yaremchuken.fitnessterra.view.schedule

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import exp.yaremchuken.fitnessterra.model.Schedule
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.utils.Utils
import java.time.LocalDate
import java.time.YearMonth


@Preview
@Composable
fun ScheduleCalendarDateView(
    width: Dp = 45.dp,
    date: LocalDate = LocalDate.now(),
    month: YearMonth = YearMonth.now(),
    scheduled: List<Schedule> = listOf(scheduleStub, scheduleStub)
) {
    val isNotInMonth = date.isBefore(month.atDay(1)) || date.isAfter(month.atEndOfMonth())

    val preview =
        if (scheduled.isEmpty()) null
        else Utils.getExercisePreview(LocalContext.current, scheduled[0].workout.sections[0].sets[0].exercise.id)

    Column(
        Modifier
            .fillMaxHeight()
            .width(width + 6.dp)
            .padding(horizontal = 1.dp, vertical = 8.dp)
            .border(
                1.dp,
                if (isNotInMonth) Color.LightGray else Color.Gray,
                shape = UIConstants.ROUNDED_CORNER
            )
            .background(
                if (scheduled.isEmpty()) Color.White else Color.LightGray,
                shape = UIConstants.ROUNDED_CORNER
            )
    ) {
        Box(
            Modifier.align(Alignment.End)
        ) {
            Box(
                Modifier
                    .background(
                        color = if (date == LocalDate.now()) Color.Red else Color.Transparent,
                        shape = UIConstants.ROUNDED_CORNER
                    )
            ) {
                Text(
                    text = "${date.dayOfMonth}".padStart(2, '0'),
                    Modifier.padding(horizontal = 4.dp),
                    style = Typography.titleLarge,
                    color =
                        if (date == LocalDate.now()) Color.White
                        else if (isNotInMonth) Color.Gray
                        else Color.Black
                )
            }
        }
        if (scheduled.isNotEmpty()) {
            Box(Modifier.padding(top = 2.dp)) {
                if (preview != null) {
                    Image(
                        bitmap = preview,
                        contentDescription = null,
                        Modifier.clip(UIConstants.ROUNDED_CORNER),
                        contentScale = ContentScale.FillHeight
                    )
                }
                if (scheduled.size > 1) {
                    Text(
                        text = "${scheduled.size}",
                        Modifier
                            .padding(all = 2.dp)
                            .align(Alignment.Center),
                        style = Typography.titleLarge,
                        fontSize = 42.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
