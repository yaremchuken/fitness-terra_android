package exp.yaremchuken.fitnessterra.ui.view.schedule.date

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val HOUR_BLOCK_HEIGHT = 60.dp

@Preview
@Composable
fun HourBlockEntry (
    onClick: () -> Unit = {},
    hour: Int = 10
) {
    Column(
        Modifier
            .height(HOUR_BLOCK_HEIGHT)
            .clickable { onClick() }
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "${hour.toString().padStart(2, '0')}:00")
            Divider()
        }
    }
}
