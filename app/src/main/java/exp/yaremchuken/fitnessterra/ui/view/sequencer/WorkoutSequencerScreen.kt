package exp.yaremchuken.fitnessterra.ui.view.sequencer

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.WorkoutSequence
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.theme.AppColor
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.ui.view.library.workout.WorkoutBlock
import exp.yaremchuken.fitnessterra.util.Utils
import exp.yaremchuken.fitnessterra.viewmodel.WorkoutSequencerViewModel

@Composable
fun WorkoutSequencerScreen(
    gotoWorkout: (workoutId: Long) -> Unit,
    viewModel: WorkoutSequencerViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val sequences = remember { mutableStateListOf<WorkoutSequence>() }

    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    LaunchedEffect(Unit) {
        viewModel.getSequences().collect { seqs ->
            sequences.clear()
            sequences.addAll(
                seqs.map { w -> viewModel.fromEntity(w) }
            )
        }
    }

    Column(
        Modifier
            .fillMaxSize()
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
                text = stringResource(R.string.workout_sequences_title),
                Modifier.align(Alignment.Center),
                style = Typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        }
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            sequences
                .forEach { seq ->
                    WorkoutSequenceBlock(
                        sequence = seq,
                        gotoWorkout = gotoWorkout
                    )
                }
        }
    }
}

@Composable
fun WorkoutSequenceBlock(
    sequence: WorkoutSequence,
    gotoWorkout: (workoutId: Long) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(all = 12.dp)
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
            Modifier
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.padding(start = 12.dp))
            Image(
                painter = painterResource(R.drawable.ic_clock),
                contentDescription = null,
                Modifier
                    .width(24.dp)
                    .height(24.dp)
            )
            Text(
                text =
                Utils.TIME_FORMAT.format(sequence.scheduledAt),
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
        }
        Divider()
        Column(
            Modifier.fillMaxWidth()
        ) {
            sequence.workouts.forEach {
                WorkoutBlock(onClick = { gotoWorkout(it.id) }, workout = it)
            }
        }
    }
}