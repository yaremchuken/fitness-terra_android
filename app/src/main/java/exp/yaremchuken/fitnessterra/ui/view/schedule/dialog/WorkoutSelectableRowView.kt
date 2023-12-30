package exp.yaremchuken.fitnessterra.ui.view.schedule.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.data.model.Workout
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.ui.view.workout.workoutStub
import exp.yaremchuken.fitnessterra.util.Utils


@Preview
@Composable
fun WorkoutSelectableRowView(
    onClick: () -> Unit = {},
    selected: Boolean = true,
    workout: Workout = workoutStub
) {
    val preview = Utils.getWorkoutPreview(LocalContext.current, workout)

    Row(
        Modifier
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = UIConstants.ROUNDED_CORNER
            )
            .background(
                color = if (selected) Color.LightGray else Color.White,
                shape = UIConstants.ROUNDED_CORNER
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            bitmap = preview,
            contentDescription = null,
            Modifier
                .height(64.dp)
                .clip(UIConstants.ROUNDED_CORNER)
        )
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                Modifier
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = workout.title,
                    style = Typography.titleLarge
                )
            }
        }
    }
}