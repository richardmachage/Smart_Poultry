package com.example.smartpoultry.presentation.screens.analytics

import androidx.lifecycle.ViewModel
import com.example.smartpoultry.domain.reports.Report
import com.example.smartpoultry.presentation.uiModels.ChartClass
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GraphsViewModel @Inject constructor(
    private  val report: Report
) : ViewModel() {
    fun onExportToPdf(
        name : String,
        content : List<ChartClass>,
        reportType : String
    ){
        report.createAndSavePDF(name = name, content = content, reportType = reportType )
    }
}