package exp.yaremchuken.fitnessterra.ui.view.exercisesetup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.theme.AppColor
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@Composable
fun SetsSetupBlock(
    adjust: (value: Long, idx: Int) -> Unit,
    sets: List<Long>
) {
    val scrollState = rememberScrollState()

    Divider()
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.sets_title),
            Modifier.padding(end = 12.dp),
            style = Typography.titleLarge
        )
        Row(
            Modifier.horizontalScroll(scrollState)
        ) {
            sets.forEachIndexed { idx, it ->
                SetBlock(
                    adjust = { v -> adjust(v, idx) },
                    remove = { adjust(-it, idx) },
                    idx = idx,
                    set = it
                )
            }
            Column(
                Modifier
                    .clickable { adjust(sets.last(), sets.size) }
                    .fillMaxHeight()
                    .padding(start = 8.dp)
                    .border(1.dp, Color.LightGray, UIConstants.ROUNDED_CORNER),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(all = 12.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "+",
                        style = Typography.headlineLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun SetBlock(
    adjust: (value: Long) -> Unit,
    remove: () -> Unit,
    idx: Int,
    set: Long
) {
    var markTrash by remember { mutableStateOf(false) }
    var countdown by remember { mutableStateOf(0.seconds) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(200.milliseconds)
            if (countdown > 0.seconds) {
                countdown -= 200.milliseconds
            }
            if (countdown <= 0.seconds) {
                countdown = 0.seconds
                markTrash = false
            }
        }
    }

    Column (
        Modifier
            .padding(start = if (idx == 0) 0.dp else 4.dp)
            .width(IntrinsicSize.Max)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .clickable { adjust(1) }
                .border(
                    1.dp,
                    Color.LightGray,
                    RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                )
                .background(
                    AppColor.LightestGray,
                    RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                )
        ) {
            Text(
                text = "+",
                Modifier
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .align(Alignment.Center),
                style = Typography.titleMedium,
                textAlign = TextAlign.Center,
                color = Color.DarkGray
            )
        }
        Box(
            Modifier
                .clickable {
                    markTrash =
                        if (markTrash) {
                            remove()
                            false
                        } else {
                            countdown = 2.seconds
                            true
                        }
                }
                .border(1.dp, AppColor.GrayTransparent)
        ) {
            if (markTrash) {
                Image(
                    painter = painterResource(id = R.drawable.ic_delete_icon),
                    contentDescription = null,
                    Modifier
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                        .height(28.dp)
                        .width(28.dp)
                )
            } else {
                Text(
                    text = "$set",
                    Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
                    style = Typography.titleLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
        Box(
            Modifier
                .fillMaxWidth()
                .clickable { adjust(-1) }
                .border(
                    1.dp,
                    Color.LightGray,
                    RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                )
                .background(
                    AppColor.LightestGray,
                    RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                )
        ) {
            Text(
                text = "-",
                Modifier
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .align(Alignment.Center),
                style = Typography.titleMedium,
                color = Color.DarkGray
            )
        }
    }
}