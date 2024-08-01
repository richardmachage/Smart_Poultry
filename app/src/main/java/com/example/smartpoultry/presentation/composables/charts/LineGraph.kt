package com.example.smartpoultry.presentation.composables.charts

import androidx.compose.runtime.Composable
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer

@Composable
fun LineGraph(
    chartEntryModel : ChartEntryModelProducer,
    startAxisTitle : String,
    //startAxisValueFormatter: AxisValueFormatter<AxisPosition.Vertical.Start>,
    bottomAxisTitle: String,
    //bottomAxisValueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom>

){
   Chart(
       chart = lineChart(),
       chartModelProducer = chartEntryModel,
       startAxis = rememberStartAxis(
           //valueFormatter = startAxisValueFormatter,
           titleComponent = textComponent(),
           title = startAxisTitle),
       bottomAxis = rememberBottomAxis(
           //valueFormatter = bottomAxisValueFormatter,
           //titleComponent = textComponent(),
           title = bottomAxisTitle
       )
   )
}