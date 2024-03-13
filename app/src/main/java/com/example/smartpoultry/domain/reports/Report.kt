package com.example.smartpoultry.domain.reports

import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.provider.MediaStore
import com.example.smartpoultry.presentation.uiModels.ChartClass
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.text.SimpleDateFormat
import javax.inject.Inject


class Report @Inject constructor(
    @ApplicationContext private val context : Context
) {

    private fun addTextToPage(page: PdfDocument.Page, listOfRecords : List<ChartClass>, startX: Float,startY: Float, paint: Paint, reportType: String){
        //default title settings
        val title = "ABUYA POULTRY FARM"
        val titlePaint = Paint(paint)
        titlePaint.textAlign = Paint.Align.CENTER
        titlePaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        titlePaint.textSize = 20f
        //center of the page
        val centerX = page.info.pageWidth / 2f
        //drawing title at center top
        page.canvas.drawText(title,centerX,startY,titlePaint)

        //report type
        val reportTypePaint = Paint(paint).apply {
            isUnderlineText = true
            textSize = 16f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            textAlign = Paint.Align.LEFT
        }
        var y = startY + titlePaint.descent() - titlePaint.ascent() + 20
        page.canvas.drawText(reportType,startX,y, reportTypePaint)

        //default date
        val currentDate = "Date : "+ SimpleDateFormat("dd MMMM, yyyy").format(System.currentTimeMillis())
        val datePaint = Paint(paint).apply {
            textSize = 14f
            typeface = Typeface.create(Typeface.DEFAULT,Typeface.NORMAL)
            textAlign = Paint.Align.LEFT
        }
        //adjust y-coordinate as usual
        y+= reportTypePaint.descent() - titlePaint.ascent() + 2
        page.canvas.drawText(currentDate,startX,y,datePaint)



        //for rest of the content
        y+= datePaint.descent() - titlePaint.ascent() + 10
        //pdf content
        paint.textAlign = Paint.Align.LEFT
        //iterate over records and draw them
        for (record in listOfRecords){
            val text = "${record.xDateValue} - ${record.yNumOfEggs} eggs"
            page.canvas.drawText(text,startX,y,paint)
            y += paint.descent() - paint.ascent()
        }

    }
    private fun addTextToPage(page: PdfDocument.Page, text: String, startX: Float, startY: Float, paint: Paint, reportType: String) {

        //default title settings
        val title = "ABUYA POULTRY FARM"
        val titlePaint = Paint(paint)
        titlePaint.textAlign = Paint.Align.CENTER
        titlePaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        titlePaint.textSize = 20f
        //center of the page
        val centerX = page.info.pageWidth / 2f

        //drawing title at center top
        page.canvas.drawText(title,centerX,startY,titlePaint)


        //report type
        val reportTypePaint = Paint(paint).apply {
            isUnderlineText = true
            textSize = 16f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            textAlign = Paint.Align.LEFT
        }
        // Adjust y-coordinate for the report type (below the title with some padding)
        var y = startY + titlePaint.descent() - titlePaint.ascent() + 20

        page.canvas.drawText(reportType,startX,y, reportTypePaint)


        //default date
        val currentDate = "Date : "+ SimpleDateFormat("dd MMMM, yyyy").format(System.currentTimeMillis())
        val datePaint = Paint(paint).apply {
            textSize = 14f
            typeface = Typeface.create(Typeface.DEFAULT,Typeface.NORMAL)
            textAlign = Paint.Align.LEFT
        }
        //adjust y-coordinate as usual
        y+= reportTypePaint.descent() - titlePaint.ascent() + 2
        page.canvas.drawText(currentDate,startX,y,datePaint)



        //for rest of the content
        y+= datePaint.descent() - titlePaint.ascent() + 10
        //pdf content
        paint.textAlign = Paint.Align.LEFT
        val lines = text.split("\n")

        for (line in lines){
            page.canvas.drawText(line,startX,y,paint)
            y += paint.descent() - paint.ascent()
        }

    }

    fun createAndSavePDF(name : String, content: List<ChartClass>, reportType: String) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() //A4 size
        val page = pdfDocument.startPage(pageInfo)

        //adding text to doc
        //  val canvas = page.canvas
        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 14f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }
        //canvas.drawText("Hello, PDF World!", 50f, 50f, paint)

        addTextToPage(page = page, listOfRecords = content, startX = 50f, startY = 50f, paint, reportType)

        pdfDocument.finishPage(page)

        //save pdf file
        savePdfWithMediaStore(pdfDocument, name)


    }

    fun createAndSavePDF(name : String, content: String, reportType: String) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() //A4 size
        val page = pdfDocument.startPage(pageInfo)

        //adding text to doc
      //  val canvas = page.canvas
        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 14f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }
        //canvas.drawText("Hello, PDF World!", 50f, 50f, paint)

        addTextToPage(page = page, text = content, startX = 50f, startY = 50f, paint, reportType)

        pdfDocument.finishPage(page)

        //save pdf file
        savePdfWithMediaStore(pdfDocument, name)


    }

    private fun savePdfWithMediaStore(pdfDocument: PdfDocument, fileName: String) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName) // File name
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf") // File type
            // Save file to Downloads directory
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

        try {
            uri?.let { documentUri ->
                resolver.openOutputStream(documentUri).use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }
            } ?: throw IOException("Failed to create new MediaStore record.")
        } catch (e: IOException) {
            e.printStackTrace()
        }


    }


    private fun calculateOffsetForTitleAndReportType(titlePaint: Paint, reportTypePaint: Paint, datePaint: Paint): Float {
        val titleHeight = titlePaint.descent() - titlePaint.ascent()
        val reportTypeHeight = reportTypePaint.descent() - reportTypePaint.ascent()
        val dateHeight = datePaint.descent() - datePaint.ascent()

        // Sum the heights and add some padding between title, report type, and date
        return titleHeight + reportTypeHeight + dateHeight + 32 // 60 is the total padding
    }

    private fun calculateLineHeight(paint: Paint): Float {
        // The line height is the distance from the descent of one line of text to the ascent of the next line.
        return paint.descent() - paint.ascent()
    }


}