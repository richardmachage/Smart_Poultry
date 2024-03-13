package com.example.smartpoultry.presentation.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
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

        Column (

            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(
                        (0.03 * LocalConfiguration.current.screenWidthDp).dp
                    )
                )
                .fillMaxWidth()
                .padding(6.dp)
        ){


            MyVerticalSpacer(height = 5)
            Text(text = "Column Chart: ")
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

            Text(text = "Line Chart: ")
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

            MyVerticalSpacer(height = 10)

            NormButton(
                modifier = Modifier.fillMaxWidth(),
                onButtonClick = {

                },
                btnName = "Export Data to PDF>>>"
            )
        }
    }
}