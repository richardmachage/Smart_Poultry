package com.example.smartpoultry.presentation.composables

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import com.example.smartpoultry.data.dataModels.DailyEggCollection
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
        Text(text = "Column Chart")

        Chart(
            chart = columnChart(),
            model = chartEntryModel,
            startAxis = rememberStartAxis(
                valueFormatter = verticalAxisValueFormatter,
                titleComponent = textComponent().apply { color = MaterialTheme.colorScheme.primary.toArgb() },
                title = "Number of Eggs",
                itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = 6)
            ),
            bottomAxis = rememberBottomAxis(
                valueFormatter = horizontalAxisValueFormatter,
                titleComponent = textComponent().apply { color = MaterialTheme.colorScheme.primary.toArgb() },
                title = "Date",
                //guideline = null
            ),
            isZoomEnabled = true
            
        )
        
        MyVerticalSpacer(height = 5)
        Text(text = "LineChart")

        Chart(
            chart = lineChart(),
            model = chartEntryModel,
            startAxis = rememberStartAxis(
                valueFormatter = verticalAxisValueFormatter,
                titleComponent = textComponent().apply { color = MaterialTheme.colorScheme.primary.toArgb() },
                title = "Number of Eggs",
                itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = 6)
            ),
            bottomAxis = rememberBottomAxis(
                valueFormatter = horizontalAxisValueFormatter,
                titleComponent = textComponent().apply { color = MaterialTheme.colorScheme.primary.toArgb() },
                title = "Date",
                //guideline = null
            ),
            isZoomEnabled = true

        )




}