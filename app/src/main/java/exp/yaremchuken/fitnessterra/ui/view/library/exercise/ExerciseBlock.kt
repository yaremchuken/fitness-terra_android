package exp.yaremchuken.fitnessterra.ui.view.library.exercise

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.Exercise
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.theme.AppType
import exp.yaremchuken.fitnessterra.ui.view.animation.ExerciseAnimation

@Composable
fun ExerciseBlock(
    onClick: () -> Unit,
    exercise: Exercise
) {
    Column(
        Modifier
            .clickable { onClick() }
            .background(Color.White)
            .padding(top = 12.dp, bottom = 8.dp, start = 12.dp, end = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExerciseAnimation(
                exercise,
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
                        text = exercise.title,
                        style = AppType.titleMedium
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
                    Image(
                        painter = painterResource(id = R.drawable.ic_forward_filled),
                        contentDescription = null)
                }
            }
        }
        Divider(Modifier.padding(top = 20.dp))
    }
}