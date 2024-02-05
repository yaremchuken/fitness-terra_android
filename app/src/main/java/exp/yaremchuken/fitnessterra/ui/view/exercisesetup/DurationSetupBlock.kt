package exp.yaremchuken.fitnessterra.ui.view.exercisesetup

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.util.Utils
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

private val TIME_BLOCK_HEIGHT = 40.dp

private val TIME_BLOCK_WIDTH = 50.dp

private class TimeUnitScrollState(
    val state: ScrollState,
    initTimeUnit: Int
) {
    var wasScrolled = false
    var scrollStartingPoint = 0
    var timeUnit: Int

    init {
        timeUnit = initTimeUnit
    }
}

@Composable
fun DurationSetupBlock(
    adjust: (value: Duration) -> Unit,
    title: String,
    duration: Duration
) {
    var init by remember { mutableStateOf(false) }

    val secScrollState = rememberScrollState()
    val minScrollState = rememberScrollState()
    val hourScrollState = rememberScrollState()

    val timeScrolls = remember { mutableStateMapOf<DurationUnit, TimeUnitScrollState>() }

    val blockHeightPx = Utils.dpToPx(TIME_BLOCK_HEIGHT.value, LocalContext.current)

    val secTitle = stringResource(id = R.string.second_short_title)
    val minTitle = stringResource(id = R.string.minute_short_title)
    val hrTitle = stringResource(id = R.string.hour_short_title)

    LaunchedEffect(Unit) {
        if (!init) {
            timeScrolls[DurationUnit.SECONDS] =
                TimeUnitScrollState(secScrollState, (duration.inWholeSeconds % 60).toInt())
            timeScrolls[DurationUnit.MINUTES] =
                TimeUnitScrollState(minScrollState, (duration.inWholeMinutes % 60).toInt())
            timeScrolls[DurationUnit.HOURS] =
                TimeUnitScrollState(hourScrollState, duration.inWholeHours.toInt())

            secScrollState.scrollTo(timeScrolls[DurationUnit.SECONDS]!!.timeUnit * blockHeightPx)
            minScrollState.scrollTo(timeScrolls[DurationUnit.MINUTES]!!.timeUnit * blockHeightPx)
            hourScrollState.scrollTo(timeScrolls[DurationUnit.HOURS]!!.timeUnit * blockHeightPx)

            init = true
        }

        while (true) {
            timeScrolls.forEach { (key, scroll) ->
                if (scroll.state.isScrollInProgress && !scroll.wasScrolled) {
                    scroll.wasScrolled = true
                    scroll.scrollStartingPoint = scroll.state.value
                }
                if (!scroll.state.isScrollInProgress && scroll.wasScrolled) {
                    scroll.wasScrolled = false
                    val isDirUp = scroll.state.value > scroll.scrollStartingPoint
                    var scrolledToUnit = scroll.state.value / blockHeightPx + if (isDirUp) 1 else 0
                    if (key == DurationUnit.SECONDS &&
                        scrolledToUnit == 0 &&
                        timeScrolls[DurationUnit.HOURS]!!.timeUnit == 0 &&
                        timeScrolls[DurationUnit.MINUTES]!!.timeUnit == 0
                    ) {
                        scrolledToUnit = 1
                    }
                    scroll.state.animateScrollTo(scrolledToUnit * blockHeightPx)
                    timeScrolls[key]!!.timeUnit = scrolledToUnit

                    adjust(
                        timeScrolls[DurationUnit.HOURS]!!.timeUnit.hours +
                        timeScrolls[DurationUnit.MINUTES]!!.timeUnit.minutes +
                        timeScrolls[DurationUnit.SECONDS]!!.timeUnit.seconds
                    )
                }
            }
            delay(100)
        }
    }

    if (!init) {
        return
    }

    Divider()
    Row(
        Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            Modifier.padding(end = 12.dp),
            style = Typography.titleLarge
        )
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf(DurationUnit.HOURS, DurationUnit.MINUTES, DurationUnit.SECONDS).forEach {
                    val scroll = timeScrolls[it]!!
                    if (it != DurationUnit.HOURS) {
                        Text(
                            text = ":",
                            style = Typography.headlineLarge
                        )
                    }
                    Box(
                        Modifier
                            .height(IntrinsicSize.Min)
                            .padding(
                                start = if (it == DurationUnit.HOURS) 0.dp else 12.dp,
                                end = if (it == DurationUnit.SECONDS) 0.dp else 12.dp
                            )
                    ) {
                        Column(
                            Modifier
                                .height(TIME_BLOCK_HEIGHT * 3)
                                .verticalScroll(scroll.state)
                        ) {
                            for(i in -1 .. 60) {
                                Text(
                                    text = if (i == -1 || i == 60) "" else i.toString().padStart(2, '0'),
                                    Modifier
                                        .height(TIME_BLOCK_HEIGHT)
                                        .padding(horizontal = 8.dp),
                                    style = Typography.headlineLarge,
                                    fontSize = 30.sp,
                                    color = if (scroll.state.isScrollInProgress) Color.Blue else Color.Black
                                )
                            }
                        }
                        Column(
                            Modifier
                                .fillMaxHeight()
                                .width(TIME_BLOCK_WIDTH),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                Modifier
                                    .height(TIME_BLOCK_HEIGHT)
                                    .fillMaxWidth()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color.White,
                                                Color.White,
                                                Color.Transparent
                                            )
                                        )
                                    )
                            ) { }
                            Divider(color = Color.Blue)
                            Row(
                                Modifier
                                    .height(TIME_BLOCK_HEIGHT)
                                    .fillMaxWidth()
                            ) { }
                            Divider(color = Color.Blue)
                            Row(
                                Modifier
                                    .height(TIME_BLOCK_HEIGHT)
                                    .fillMaxWidth()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color.Transparent,
                                                Color.White,
                                                Color.White
                                            )
                                        )
                                    )
                            ) { }
                        }
                        Text(
                            text = when(it) {
                                DurationUnit.SECONDS -> secTitle
                                DurationUnit.MINUTES -> minTitle
                                DurationUnit.HOURS -> hrTitle
                                else -> throw IllegalArgumentException("Unknown duration unit $it")
                            },
                            Modifier.width(TIME_BLOCK_WIDTH),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}