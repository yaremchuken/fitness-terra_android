package exp.yaremchuken.fitnessterra.ui.view.exercise

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.ui.theme.Typography

@Preview
@Composable
fun ExerciseStepView(
    index: Int = 1,
    description: String = exerciseBicepsStub.steps[0]
) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 14f), 0f)

    Row(
        Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .padding(
                top = if (index == 1) 0.dp else 8.dp
            )
    ) {
        Column {
            Text(
                text = "$index".padStart(2, '0'),
                style = Typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Magenta
            )
        }
        Column {
            Spacer(Modifier.padding(top = 3.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_exercise_step),
                contentDescription = null,
                Modifier
                    .height(26.dp)
                    .padding(horizontal = 5.dp)
            )
            Canvas(
                Modifier
                    .fillMaxHeight()
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(vertical = 5.dp)
            ) {
                drawLine(
                    color = Color.Magenta,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    pathEffect = pathEffect
                )
            }
        }
        Column {
            Text(
                text = description,
                Modifier.padding(top = 1.dp),
                style = Typography.bodyMedium
            )
        }
    }
}
