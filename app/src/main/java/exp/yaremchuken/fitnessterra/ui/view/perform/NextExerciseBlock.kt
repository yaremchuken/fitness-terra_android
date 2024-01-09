package exp.yaremchuken.fitnessterra.ui.view.perform

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.Exercise
import exp.yaremchuken.fitnessterra.data.model.WorkoutSection
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.ui.view.animation.ExerciseAnimation
import exp.yaremchuken.fitnessterra.ui.view.workout.workoutStub


@Preview
@Composable
fun NextExerciseBlock(
    section: WorkoutSection = workoutStub.sections[0],
    exercise: Exercise = workoutStub.sections[0].sets[0].exercise,
    currIdx: Int = 1,
    total: Int = 4
) {
    Column(
        Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier.padding(start = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.workout_perform_next_is),
                Modifier.padding(end = 8.dp),
                style = Typography.titleLarge
            )
            Text(
                text = "${section.title} $currIdx ${stringResource(R.string.from)} $total",
                style = Typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            Modifier.padding(start = 12.dp)
        ) {
            Text(
                text = exercise.titleLocalized(),
                style = Typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            Modifier
                .fillMaxSize()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExerciseAnimation(exercise, Modifier)
        }
    }
}
