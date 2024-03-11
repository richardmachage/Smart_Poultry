package com.example.smartpoultry.domain.reports

import android.graphics.pdf.PdfDocument
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class Report @Inject constructor(
    @ApplicationContext context: ApplicationContext
) {
    companion object{
        const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1
    }

    fun createAndSavePDF(){
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        val page = pdfDocument.startPage(pageInfo)



    }
}