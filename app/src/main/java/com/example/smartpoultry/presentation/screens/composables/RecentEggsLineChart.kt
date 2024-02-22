package com.example.smartpoultry.presentation.screens.composables

import android.annotation.SuppressLint
import android.icu.text.Transliterator.Position
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.example.smartpoultry.data.dataModels.DailyEggCollection
import com.example.smartpoultry.presentation.uiModels.ChartClass
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.component.text.textComponent
import kotlin.math.roundToInt


@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecentEggsLineChart(dailyEggCollections: List<DailyEggCollection>) {
    val turnToChartData = dailyEggCollections.map {  (date, totalEggs) ->
        ChartClass(xDateValue = SimpleDateFormat("dd-MMM").format(date), yNumOfEggs = totalEggs)
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
            turnToChartData.getOrNull((value).toInt())?.xDateValue?: ""
        }

    val verticalAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Vertical.Start>{value, chartValues ->
            value.roundToInt().toString()
        }

    Chart(
        chart = lineChart(

        ),
        model = chartEntryModel,
        startAxis = rememberStartAxis(
            valueFormatter = verticalAxisValueFormatter,
            titleComponent = textComponent(),
            title = "Number of Eggs"
            ),
        bottomAxis = rememberBottomAxis(
            valueFormatter = horizontalAxisValueFormatter,
            titleComponent = textComponent(),
            title = "Date",
        ),
        isZoomEnabled = true


    )


}