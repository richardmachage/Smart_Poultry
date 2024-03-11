package com.example.smartpoultry.domain.reports

import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject


class Report @Inject constructor(
    @ApplicationContext private val context : Context
) {

    fun createAndSavePDF(name : String) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        //adding text to doc
        val canvas = page.canvas
        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 12f
        }
        canvas.drawText("Hello, PDF World!", 50f, 50f, paint)
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

}