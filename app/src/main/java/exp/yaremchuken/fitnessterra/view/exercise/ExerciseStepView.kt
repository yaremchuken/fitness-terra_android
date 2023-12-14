package exp.yaremchuken.fitnessterra.view.exercise

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.ui.theme.Typography


@Preview
@Composable
fun ExerciseStepView(
    index: Int = 1,
    description: String = exerciseStub.steps[0]
) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 14f), 0f)

    Row(
        Modifier
            .fillMaxWidth()
            .padding(
                top = if (index == 1) 0.dp else 8.dp
            )
    ) {
        Column {
            Text(
                text = "$index".padStart(2, '0'),
                style = Typography.bodyLarge,
                color = Color.Magenta
            )
        }
        Column {
            Image(
                painter = painterResource(id = R.drawable.ic_exercise_steps_divider),
                contentDescription = null,
                Modifier.padding(horizontal = 5.dp)
            )
            Canvas(
                Modifier
                    .height(40.dp)
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
//            Image(
//                painter = painterResource(id = R.drawable.img_dotted_line_divider),
//                contentDescription = null,
//                Modifier
//                    .align(alignment = Alignment.CenterHorizontally)
//                    .padding(vertical = 3.dp),
//                contentScale = ContentScale.FillHeight,
//            )
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
