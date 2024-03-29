package exp.yaremchuken.fitnessterra.ui.view.schedule.date

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.Workout
import exp.yaremchuken.fitnessterra.getHour
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.theme.AppColor
import exp.yaremchuken.fitnessterra.ui.theme.AppType
import exp.yaremchuken.fitnessterra.util.Utils
import java.time.Instant

@Composable
fun ScheduledWorkoutBlockView(
    onClick: () -> Unit,
    scheduledAt: Instant,
    workout: Workout,
    isEditable: Boolean = false,
    isHistory: Boolean = false
) {
    Row(
        Modifier
            .height(HOUR_BLOCK_HEIGHT)
            .fillMaxWidth()
            .offset(y = HOUR_BLOCK_HEIGHT * scheduledAt.getHour() + 10.dp)
            .clickable { onClick() }
            .background(
                color = if (isEditable) Color.Cyan else AppColor.LightestGray,
                shape = UIConstants.ROUNDED_CORNER
            )
    ) {
        if (isHistory) {
            Column(
                Modifier.fillMaxHeight()
                    .padding(all = 6.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_finish),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
            }
        } else if (isEditable) {
            Column(
                Modifier
                    .width(12.dp)
                    .fillMaxHeight()
                    .background(
                        color = Color.Blue,
                        shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                    )
            ) { }
        } else {
            Column(
                Modifier
                    .width(12.dp)
                    .fillMaxHeight()
                    .background(
                        color = Color.Magenta,
                        shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                    )
            ) { }
        }
        Column(
            Modifier.padding(start = 8.dp, top = 6.dp, bottom = 2.dp)
        ) {
            Text(
                text = Utils.TIME_FORMAT.format(scheduledAt) + " • " +
                        Utils.formatToTime(workout.totalDuration())
            )
            Text(
                text = workout.title,
                style = AppType.titleMedium
            )
        }
    }
}
