package com.forsythe.smartpoultry.domain.reports

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import com.forsythe.smartpoultry.data.dataModels.EggRecordFull
import com.forsythe.smartpoultry.utils.format
import com.forsythe.smartpoultry.utils.toUtilDate
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook

fun createWorkBook(
    context: Context,
    sheetName : String,
    listOfRecords : List<EggRecordFull>
) : XSSFWorkbook{
    val workbook = XSSFWorkbook()
    //let each sheet be named after a block...then
    val sheet = workbook.createSheet(sheetName)

    val headerRow = sheet.createRow(0)
   // headerRow.createCell(0).setCellValue("Record Id")
    headerRow.createCell(0).setCellValue("Date")
    headerRow.createCell(1).setCellValue("Block")
    headerRow.createCell(2).setCellValue("Cell")
    headerRow.createCell(3).setCellValue("Egg Count")
    headerRow.createCell(4).setCellValue("Hen Count")


    //populating the sheet
    for ((index, record) in listOfRecords.withIndex()){
        val row = sheet.createRow(index + 1)
        row.addEggRecord(record)
    }
    return workbook
}

fun saveWorkbookWithMediaStore(context: Context, workbook: XSSFWorkbook, fileName: String){
    // Determine the location and MIME type
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName) // File name
        put(MediaStore.MediaColumns.MIME_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        // Save file to Downloads directory
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
    }
    // Insert the file into MediaStore
    val resolver = context.contentResolver

    val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

    try {
        uri?.let {
            resolver.openOutputStream(it).use { outputStream ->
                workbook.write(outputStream)
                outputStream?.flush()
            }
            workbook.close()

        } ?: run {
            // Handle the case where the file could not be created
            workbook.close()
            throw IllegalStateException("Unable to create file in MediaStore")
        }
    }catch (e : Exception){
        Throwable(e)
        //Log.d("WorkBook", "saveWorkbookWithMediaStore: ${e.message}")
    }

}
fun Row.addEggRecord(record : EggRecordFull){
    createCell(0).setCellValue(record.date.toUtilDate().format())
    createCell(1).setCellValue(record.blockNum.toDouble())
    createCell(2).setCellValue(record.cellNum.toDouble())
    createCell(3).setCellValue(record.eggCount.toDouble())
    createCell(4).setCellValue(record.henCount.toDouble())
}