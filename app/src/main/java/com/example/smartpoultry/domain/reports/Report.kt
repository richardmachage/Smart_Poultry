package com.example.smartpoultry.domain.reports

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject


class Report @Inject constructor(
    //@ApplicationContext val context: ApplicationContext
    private val context : Context
) {
    companion object{
        const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1
    }

    fun createAndSavePDF(){
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


        //decision on storage Location based on android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            savePdfToInternalStorage(pdfDocument, "example.pdf")
        } else {
            //TODO
            if(ContextCompat.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
                //TODO Permission request result to be handled elsewhere, e.g., onRequestPermissionsResult
            }else{
                savePdfToExternalStorage(pdfDocument, "example.pdf")
            }
        }



    }

    private fun savePdfToExternalStorage(pdfDocument: PdfDocument, fileName: String) {
        val externalFilesDir = context.getExternalFilesDir(null)
        if (externalFilesDir != null) {
            val file = File(externalFilesDir, fileName)
            try {
                FileOutputStream(file).use { output ->
                    pdfDocument.writeTo(output)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                pdfDocument.close()
            }
        }
    }

    private fun savePdfToInternalStorage(pdfDocument: PdfDocument, fileName: String) {
        try {
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
                pdfDocument.writeTo(output)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            pdfDocument.close()
        }
    }
}