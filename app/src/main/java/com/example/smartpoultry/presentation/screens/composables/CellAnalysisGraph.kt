package com.example.smartpoultry.presentation.screens.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import com.example.smartpoultry.presentation.uiModels.ChartClass
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

@Composable
fun CellAnalysisGraph(
    isGraphPlotted : Boolean,
    listOfRecords : List<ChartClass>,
    itemPlacerCount: Int,
    startAxisTitle : String,
    bottomAxisTitle: String,

    ) {
    if (isGraphPlotted) {
        //convert the input data into chartData for plotting
        val chartData = listOfRecords.mapIndexed { index, (_, numOfEggs) ->
            entryOf(
                index.toFloat(),
                numOfEggs.toFloat()
            ) // use index as x value, convert numOfEggs to float
        }

        //Build the chartEntryModel
        val chartEntryModel = entryModelOf(*chartData.map { it.x to it.y }.toTypedArray())

        val horizontalAxisValueFormatter =
            AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
                listOfRecords.getOrNull((value).toInt())?.xDateValue ?: ""
            }

        val verticalAxisValueFormatter =
            AxisValueFormatter<AxisPosition.Vertical.Start> { value, chartValues ->
                value.roundToInt().toString()
            }

        Chart(
            chart = lineChart(),
            model = chartEntryModel,
            startAxis = rememberStartAxis(
                valueFormatter = verticalAxisValueFormatter,
                titleComponent = textComponent().apply {
                    color = MaterialTheme.colorScheme.primary.toArgb()
                },
                title = startAxisTitle,
                itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = itemPlacerCount)
            ),
            bottomAxis = rememberBottomAxis(
                valueFormatter = horizontalAxisValueFormatter,
                titleComponent = textComponent().apply {
                    color = MaterialTheme.colorScheme.primary.toArgb()
                },
                title = bottomAxisTitle
            ),
            isZoomEnabled = true,
        )

        MyVerticalSpacer(height = 5)
        Chart(
            chart = columnChart(),
            model = chartEntryModel,
            startAxis = rememberStartAxis(
                valueFormatter = verticalAxisValueFormatter,
                titleComponent = textComponent().apply {
                    color = MaterialTheme.colorScheme.primary.toArgb()
                },
                title = startAxisTitle,
                itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = itemPlacerCount)
            ),
            bottomAxis = rememberBottomAxis(
                valueFormatter = horizontalAxisValueFormatter,
                titleComponent = textComponent().apply {
                    color = MaterialTheme.colorScheme.primary.toArgb()
                },
                title = bottomAxisTitle
            ),
            isZoomEnabled = true,
        )
    }
}