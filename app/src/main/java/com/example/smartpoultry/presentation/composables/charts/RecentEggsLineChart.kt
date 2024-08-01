package com.example.smartpoultry.presentation.composables.charts

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.example.smartpoultry.R
import com.example.smartpoultry.data.dataModels.DailyEggCollection
import com.example.smartpoultry.presentation.composables.MyOutlineButton
import com.example.smartpoultry.presentation.uiModels.ChartClass
import com.example.smartpoultry.utils.toGraphDate
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import kotlin.math.roundToInt


@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecentEggsLineChart(dailyEggCollections: List<DailyEggCollection>) {
    val turnToChartData = dailyEggCollections.map { (date, totalEggs) ->
        ChartClass(xDateValue = toGraphDate(date), yNumOfEggs = totalEggs)
    }

    val chartData = turnToChartData.mapIndexed { index, (_, numOfEggs) ->
        entryOf(
            index.toFloat(),
            numOfEggs.toFloat()
        ) // use index as x value, convert numOfEggs to float
    }

    val chartEntryModel = entryModelOf(*chartData.map { it.x to it.y }
        .toTypedArray())// i wil have to get back here to understand this part correctly, im sure i'll forget

    val horizontalAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
            turnToChartData.getOrNull((value).toInt())?.xDateValue ?: ""
        }

    val verticalAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Vertical.Start> { value, chartValues ->
            value.roundToInt().toString()
        }

       // Text(text = "Column Chart")

    Column {
        var showLineChart by remember{ mutableStateOf(true) }

        AnimatedVisibility(visible = !showLineChart) {
            Chart(
                chart = columnChart(),
                model = chartEntryModel,
                startAxis = rememberStartAxis(
                    valueFormatter = verticalAxisValueFormatter,
                    titleComponent = textComponent().apply {
                        color = MaterialTheme.colorScheme.primary.toArgb()
                    },
                    title = "Number of Eggs",
                    itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = 6)
                ),
                bottomAxis = rememberBottomAxis(
                    valueFormatter = horizontalAxisValueFormatter,
                    titleComponent = textComponent().apply {
                        color = MaterialTheme.colorScheme.primary.toArgb()
                    },
                    title = "Date",
                    //guideline = null
                ),
                isZoomEnabled = true

            )
        }

        AnimatedVisibility(visible = showLineChart) {
            Chart(
                chart = lineChart(),
                model = chartEntryModel,
                startAxis = rememberStartAxis(
                    valueFormatter = verticalAxisValueFormatter,
                    titleComponent = textComponent().apply {
                        color = MaterialTheme.colorScheme.primary.toArgb()
                    },
                    title = "Number of Eggs",
                    itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = 6)
                ),
                bottomAxis = rememberBottomAxis(
                    valueFormatter = horizontalAxisValueFormatter,
                    titleComponent = textComponent().apply {
                        color = MaterialTheme.colorScheme.primary.toArgb()
                    },
                    title = "Date",
                    //guideline = null
                ),
                isZoomEnabled = true

            )

        }
        MyOutlineButton(
            modifier = Modifier.fillMaxWidth(),
            onButtonClick = { showLineChart = !showLineChart },
            btnName = stringResource(id = if (showLineChart)  R.string.show_bar_chart else R.string.show_line_chart),
            trailingIcon = ImageVector.vectorResource(id = if (!showLineChart) R.drawable.ic_line_chart else R.drawable.ic_bar_chart)
        )

    }


}