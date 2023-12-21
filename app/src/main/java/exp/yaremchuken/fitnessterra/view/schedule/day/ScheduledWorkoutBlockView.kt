package exp.yaremchuken.fitnessterra.view.schedule.day

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.getHour
import exp.yaremchuken.fitnessterra.model.Schedule
import exp.yaremchuken.fitnessterra.model.Workout
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.utils.Utils
import exp.yaremchuken.fitnessterra.view.schedule.schedulesStub

@Preview
@Composable
fun ScheduledWorkoutBlockView(
    onClick: () -> Unit = {},
    schedule: Schedule = schedulesStub[3]
) {
    Row(
        Modifier
            .height(HOUR_BLOCK_HEIGHT)
            .fillMaxWidth()
            .offset(y = HOUR_BLOCK_HEIGHT * schedule.scheduledAt.getHour() + 10.dp)
            .clickable { onClick() }
            .background(
                color = Color.Cyan,
                shape = UIConstants.ROUNDED_CORNER
            )
    ) {
        Column(
            Modifier
                .width(12.dp)
                .fillMaxHeight()
                .background(
                    color = Color.Blue,
                    shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                )
        ) { }
        Column(
            Modifier.padding(start = 8.dp, top = 6.dp, bottom = 2.dp)
        ) {
            Text(
                text = Utils.TIME_FORMAT.format(schedule.scheduledAt) + " • " +
                        Utils.formatToTime(Workout.totalDuration(schedule.workout))
            )
            Text(
                text = schedule.workout.title,
                style = Typography.titleMedium
            )
        }
    }
}