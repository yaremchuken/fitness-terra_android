package exp.yaremchuken.fitnessterra.ui.view.library.workout

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
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.data.model.Workout
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.util.Utils

@Composable
fun WorkoutBlock(
    onClick: () -> Unit,
    workout: Workout
) {
    val preview = Utils.getWorkoutPreview(LocalContext.current, workout)

    Row(
        Modifier
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = UIConstants.ROUNDED_CORNER
            )
            .background(
                color = Color.White,
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
                    .weight(1F)
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