package exp.yaremchuken.fitnessterra.ui.view.exercisesetup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.Equipment
import exp.yaremchuken.fitnessterra.ui.theme.AppColor
import exp.yaremchuken.fitnessterra.ui.theme.Typography

@Composable
fun EquipmentSetupBlock(
    adjust: (value: Long) -> Unit,
    equipment: Equipment
) {
    Divider()
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.weight_title),
            Modifier.padding(end = 12.dp),
            style = Typography.titleLarge
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (equipment.quantity > 1) {
                Text(
                    text = "${equipment.quantity} X",
                    Modifier.padding(end = 8.dp),
                    style = Typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
            }
            Column(
                Modifier
                    .width(IntrinsicSize.Max)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Row(
                        Modifier
                            .weight(1F)
                            .clickable { adjust(1000) }
                            .border(1.dp, Color.LightGray, RoundedCornerShape(topStart = 12.dp))
                            .background(AppColor.LightestGray, RoundedCornerShape(topStart = 12.dp)),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "+1 ${stringResource(R.string.kg_title)}",
                            Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = Typography.titleMedium,
                            textAlign = TextAlign.Center,
                            color = Color.DarkGray
                        )
                    }
                    Row(
                        Modifier
                            .weight(1F)
                            .clickable { adjust(100) }
                            .border(1.dp, Color.LightGray)
                            .background(AppColor.LightestGray),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "+100 ${stringResource(R.string.gr_title)}",
                            Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = Typography.titleMedium,
                            textAlign = TextAlign.Center,
                            color = Color.DarkGray
                        )
                    }
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .border(1.dp, AppColor.GrayTransparent),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "${equipment.weight / 1000} . ${equipment.weight % 1000}",
                        Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
                        style = Typography.titleLarge
                    )
                }
                Row(
                    Modifier.fillMaxWidth()
                ) {
                    Row(
                        Modifier
                            .weight(1F)
                            .clickable { if (equipment.weight > 1000) adjust(-1000) }
                            .border(1.dp, Color.LightGray, RoundedCornerShape(bottomStart = 12.dp))
                            .background(AppColor.LightestGray, RoundedCornerShape(bottomStart = 12.dp)),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "-1 ${stringResource(R.string.kg_title)}",
                            Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = Typography.titleMedium,
                            textAlign = TextAlign.Center,
                            color = Color.DarkGray
                        )
                    }
                    Row(
                        Modifier
                            .weight(1F)
                            .clickable { if (equipment.weight > 100) adjust(-100) }
                            .border(1.dp, Color.LightGray)
                            .background(AppColor.LightestGray),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "-100 ${stringResource(R.string.gr_title)}",
                            Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = Typography.titleMedium,
                            textAlign = TextAlign.Center,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}