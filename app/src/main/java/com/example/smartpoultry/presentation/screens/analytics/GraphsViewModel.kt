package com.example.smartpoultry.presentation.screens.analytics

import androidx.lifecycle.ViewModel
import com.example.smartpoultry.data.dataSource.local.datastore.PreferencesRepo
import com.example.smartpoultry.domain.reports.Report
import com.example.smartpoultry.presentation.uiModels.ChartClass
import com.example.smartpoultry.utils.FARM_NAME_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GraphsViewModel @Inject constructor(
    private  val report: Report,
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
        report.createAndSavePDF(
            name = name,
            content = content,
            reportType = reportType,
            getFarmName()
        )
    }
}