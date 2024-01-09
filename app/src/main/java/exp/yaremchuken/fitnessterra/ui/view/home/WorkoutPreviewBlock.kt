package exp.yaremchuken.fitnessterra.ui.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.Schedule
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.theme.AppColor
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.ui.view.schedule.calendar.schedulesStub
import exp.yaremchuken.fitnessterra.util.Utils

@Preview
@Composable
fun WorkoutPreviewBlock(
    schedule: Schedule = schedulesStub[1]
) {
    val preview = Utils.getWorkoutPreview(LocalContext.current, schedule.workout)

    Column(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                UIConstants.ROUNDED_CORNER
            )
            .background(
                color = AppColor.LightestGray,
                shape = UIConstants.ROUNDED_CORNER
            )
    ) {
        Row(
            Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.padding(start = 12.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_clock),
                contentDescription = null,
                Modifier
                    .width(24.dp)
                    .height(24.dp)
            )
            Text(
                text = Utils.TIME_FORMAT.format(schedule.scheduledAt),
                Modifier.padding(all = 12.dp),
                style = Typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Divider(
                Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .background(color = Color.LightGray)
            )
            Text(
                text = schedule.workout.title,
                Modifier.padding(all = 12.dp),
                style = Typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Divider()
        Row(
            Modifier
                .fillMaxWidth()
                .background(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                ),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                bitmap = preview,
                contentDescription = null,
                Modifier.clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
            )
        }
    }
}