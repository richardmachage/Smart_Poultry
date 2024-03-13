package com.example.smartpoultry.presentation.screens.analytics

import com.example.smartpoultry.domain.reports.Report
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GraphsViewModel @Inject constructor(
    private  val report: Report
) {

}