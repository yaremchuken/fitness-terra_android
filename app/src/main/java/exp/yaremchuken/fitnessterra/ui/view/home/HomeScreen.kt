package exp.yaremchuken.fitnessterra.ui.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.Schedule
import exp.yaremchuken.fitnessterra.toLocalDate
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.route.Screen
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.viewmodel.HomeViewModel
import java.time.LocalDate

@Preview
@Composable
fun HomeScreen(
    navController: NavController = NavController(LocalContext.current),
    viewModel: HomeViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val todaySchedules = remember { mutableStateListOf<Schedule>() }

    LaunchedEffect(Unit) {
        todaySchedules.clear()
        viewModel.getAllSchedules().collect {
            val filtered = it
                .map { e -> viewModel.fromEntity(e) }
                .filter { s -> !s.completed && s.scheduledAt.toLocalDate() == LocalDate.now()}
            todaySchedules.addAll(filtered)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            Modifier
                .padding(all = 12.dp)
                .verticalScroll(scrollState)
        ) {
            if (todaySchedules.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.scheduled_for_today_title),
                    Modifier.padding(bottom = 12.dp),
                    style = Typography.titleLarge
                )
            }
            todaySchedules.forEach {
                ScheduledWorkoutPreviewBlock(it.scheduledAt, it.workout)
            }
            Divider()
            Column(
                Modifier.padding(vertical = 12.dp)
            ) {
                CalendarLinkBlock(onClick = {
                    navController.navigate(Screen.SCHEDULE_CALENDAR_SCREEN.name)
                })
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
            .clickable { onClick() }
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
            onClick = { },
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
