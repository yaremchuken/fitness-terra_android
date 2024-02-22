package exp.yaremchuken.fitnessterra.ui.view.exercisesetup

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.Equipment
import exp.yaremchuken.fitnessterra.data.model.ExerciseSetup
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.element.GifImage
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.util.Utils
import exp.yaremchuken.fitnessterra.viewmodel.ExerciseSetupDetailsViewModel
import kotlin.time.Duration.Companion.seconds

@Composable
fun ExerciseSetupDetailsScreen(
    gotoExercise: (id: Long) -> Unit,
    sectionId: Long,
    exerciseId: Long,
    viewModel: ExerciseSetupDetailsViewModel = hiltViewModel()
) {
    var setup by remember { mutableStateOf<ExerciseSetup?>(null) }
    val equipment = remember { mutableStateListOf<Equipment>() }
    val sets = remember { mutableStateListOf<Long>() }
    var duration by remember { mutableStateOf(0.seconds) }
    var recovery by remember { mutableStateOf(0.seconds) }

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.getSetup(sectionId, exerciseId).collect {
            setup = viewModel.fromEntity(it)
            equipment.clear()
            equipment.addAll(Gson().fromJson(it.equipment, Array<Equipment>::class.java))
            sets.clear()
            sets.addAll(Gson().fromJson(it.sets, Array<Long>::class.java))
            duration = it.duration.seconds
            recovery = it.recovery.seconds
        }
    }

    if (setup == null) {
        return
    }

    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1F)
        ) {
            GifImage(
                Utils.exerciseGifPath(setup!!.exercise),
                Modifier.align(Alignment.Center),
                ContentScale.FillHeight
            )
            IconButton(
                onClick = { onBackPressedDispatcher?.onBackPressed() },
                Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 12.dp, start = 12.dp)
                    .width(30.dp)
                    .height(30.dp)
            ) {
                Image(painter = painterResource(id = R.drawable.ic_back_filled), contentDescription = null)
            }
        }
        Divider()
        Column(
            Modifier
                .padding(horizontal = 20.dp)
                .weight(2F)
        ) {
            Text(
                text = setup!!.exercise.title,
                Modifier.padding(vertical = 12.dp),
                style = Typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable { gotoExercise(exerciseId) }
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            UIConstants.ROUNDED_CORNER
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        Modifier
                            .weight(1F)
                            .padding(all = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.exercise_details_title),
                            Modifier
                                .padding(start = 6.dp),
                            style = Typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(
                        onClick = { gotoExercise(exerciseId) },
                        Modifier
                            .padding(end = 20.dp)
                            .height(36.dp)
                            .width(36.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Image(painter = painterResource(id = R.drawable.ic_forward_filled), contentDescription = null)
                    }
                }

                equipment.filter { it.weight > 0 }.forEach { equip ->
                    EquipmentSetupBlock(
                        adjust = { w ->
                            val replaced = viewModel.adjustEquipment(equipment, equip.type, w)
                            equipment.clear()
                            equipment.addAll(replaced)
                        },
//                        adjust = { equipment.find { e -> e.type == equip.type }.weight += it },
                        equipment = equip
                    )
                }

                if (sets.isNotEmpty()) {
                    SetsSetupBlock(
                        adjust =
                        { it, idx ->
                            if (idx == sets.size) sets.add(it)
                            else if (sets[idx] + it == 0L) {
                                if (sets.size > 1) sets.removeAt(idx)
                            }
                            else sets[idx] = sets[idx] + it
                        },
                        sets = sets
                    )
                }
                if (duration > 0.seconds) {
                    Divider()
                    DurationSetupBlock(
                        adjust = { duration = it },
                        title = stringResource(R.string.duration_title),
                        duration = duration
                    )
                }
                if (recovery > 0.seconds) {
                    Divider()
                    DurationSetupBlock(
                        adjust = { recovery = it },
                        title = stringResource(R.string.recovery_title),
                        duration = recovery
                    )
                }
            }
            Spacer(modifier = Modifier.padding(bottom = 20.dp))
        }
        Column (
            Modifier
                .fillMaxWidth()
                .background(Color.LightGray),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { viewModel.updateSetup(setup!!, equipment, sets, duration, recovery) },
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 50.dp),
                shape = UIConstants.ROUNDED_CORNER
            ) {
                Text(
                    text = stringResource(id = R.string.save_btn_title),
                    style = Typography.titleLarge
                )
            }
        }
    }
}