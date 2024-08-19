package com.forsythe.smartpoultry.presentation.screens.analytics

import androidx.lifecycle.ViewModel
import com.forsythe.smartpoultry.data.dataSource.local.datastore.PreferencesRepo
import com.forsythe.smartpoultry.domain.reports.PdfReport
import com.forsythe.smartpoultry.presentation.uiModels.ChartClass
import com.forsythe.smartpoultry.utils.FARM_NAME_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GraphsViewModel @Inject constructor(
    private  val pdfReport: PdfReport,
    private val preferencesRepo: PreferencesRepo
) : ViewModel() {

    private fun getFarmName(): String{
        return preferencesRepo.loadData(FARM_NAME_KEY)?:""
    }

    fun onExportToPdf(
        name : String,
        content : List<ChartClass>,
        reportType : String
    ){
        pdfReport.createAndSavePDF(
            name = name,
            content = content,
            reportType = reportType,
            getFarmName()
        )
    }
}