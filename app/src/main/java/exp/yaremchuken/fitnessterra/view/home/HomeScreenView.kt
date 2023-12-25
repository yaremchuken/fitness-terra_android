package exp.yaremchuken.fitnessterra.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.model.Schedule
import exp.yaremchuken.fitnessterra.toLocalDate
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.view.schedule.schedulesStub
import java.time.Instant
import java.time.LocalDate

@Preview
@Composable
fun HomeScreenView(
    schedules: List<Schedule> = schedulesStub
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        val scrollState = rememberScrollState()

        val todayWorkouts = schedules.filter { !it.completed && it.scheduledAt.toLocalDate() == LocalDate.now() }
        val futureWorkouts =
            schedules.filter { it.scheduledAt.isAfter(Instant.now()) }.sortedByDescending { it.scheduledAt }

        Column(
            Modifier
                .padding(all = 12.dp)
                .verticalScroll(scrollState)
        ) {
            if (todayWorkouts.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.scheduled_for_today_title),
                    Modifier.padding(bottom = 12.dp),
                    style = Typography.titleLarge
                )
            }
            todayWorkouts.forEach {
                WorkoutPreviewBlock(it)
            }
            Divider()
            Column(
                Modifier.padding(vertical = 12.dp)
            ) {
                CalendarLinkBlock()
            }
            Divider()
        }
    }
}

@Preview
@Composable
fun CalendarLinkBlock(
    onClick: () -> Unit = {}
) {
    Row(
        Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.LightGray,
                UIConstants.ROUNDED_CORNER
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            Modifier.padding(all = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_calendar),
                contentDescription = null,
                Modifier
                    .width(48.dp)
                    .height(48.dp)
            )
            Text(
                text = stringResource(R.string.to_workouts_schedule_title),
                Modifier.padding(start = 6.dp),
                style = Typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        IconButton(
            onClick = { onClick() },
            Modifier
                .padding(end = 20.dp)
                .height(24.dp)
                .width(24.dp)
                .align(Alignment.CenterVertically)
        ) {
            Image(painter = painterResource(id = R.drawable.ic_forward_filled), contentDescription = null)
        }
    }
}
