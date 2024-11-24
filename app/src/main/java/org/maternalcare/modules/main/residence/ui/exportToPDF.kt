package org.maternalcare.modules.main.residence.ui

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.ColumnText
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import org.maternalcare.modules.main.user.service.UserReport
import org.maternalcare.shared.ext.expectedDueDate
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

fun exportToPDF(
    context: Context,
    data: List<UserReport>,
    onFinish: (file: File) -> Unit,
    onError: (Exception) -> Unit
) {
    val titleFont: Font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18f)
    val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    try {
        val file = createFile(context)
        val document = Document(PageSize.A4.rotate())
        document.setMargins(24f, 24f, 32f, 32f)

        val writer = PdfWriter.getInstance(document, FileOutputStream(file))
        writer.setFullCompression()
        document.open()

        val titleParagraph = Paragraph("Residents Report", titleFont)
        titleParagraph.alignment = Element.ALIGN_CENTER
        document.add(titleParagraph)
        addLineSpace(document, 1)

        val headers = listOf(
            "No",
            "First Name",
            "Middle Name",
            "Last Name",
            "Date of Birth",
            "Mobile Number",
            "Address",
            "BP (MMHG)",
            "Height (cm)",
            "Weight (kg)",
            "Gravida Para",
            "Last Menstrual Period",
            "Nutritional Status",
            "Age Of Gestation",
            "Expected Due Date"
        )

        val columnWidths = floatArrayOf(
            0.3f,
            0.5f,
            0.5f,
            0.5f,
            0.5f,
            0.5f,
            0.5f,
            0.5f,
            0.5f,
            0.5f,
            0.3f,
            0.5f,
            0.5f,
            0.5f,
            0.5f,
        )

        val table = createTable(headers.size, columnWidths)

        headers.forEach {
            val cell = createCell(it, fixedHeight = 40f)
            table.addCell(cell)
        }

        fun formatDate(date: String?, inputFormat: String = "yyyy-MM-dd", outputFormat: String = "dd-MM-yyyy"): String? {
            return try {
                date?.let {
                    val inputFormatter = SimpleDateFormat(inputFormat, Locale.getDefault())
                    val parsedDate = inputFormatter.parse(it)

                    parsedDate?.let { dateObject ->
                        val outputFormatter = SimpleDateFormat(outputFormat, Locale.getDefault())
                        outputFormatter.format(dateObject)
                    }
                }
            } catch (e: Exception) {
                null
            }
        }

        fun formatExpectedDueDate(date: String?): String? {
            return try {
                date?.let {
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it)
                        ?.let { parsedDate -> dateFormatter.format(parsedDate) }
                }
            } catch (e: Exception) {
                date
            }
        }

        data.forEachIndexed { index, report ->
            val user = report.user
            val checkupReport = report.checkupReport
            val checkup = checkupReport.checkup
            val formattedDate = formatDate(user.dateOfBirth)
            val formattedLastMenstrualPeriod = formatDate(checkup.lastMenstrualPeriod)
            val formattedExpectedDueDate = formatExpectedDueDate(checkup.expectedDueDate())

            table.addCell(createCell((index + 1).toString(), fixedHeight = 35f))
            table.addCell(createCell(user.firstName, fixedHeight = 35f))
            table.addCell(createCell(user.middleName ?: "", fixedHeight = 35f))
            table.addCell(createCell(user.lastName, fixedHeight = 35f))
            table.addCell(createCell(formattedDate ?: "", fixedHeight = 35f))
            table.addCell(createCell(user.mobileNumber ?: "", fixedHeight = 35f))
            table.addCell(createCell(user.address ?: "", fixedHeight = 35f))
            table.addCell(createCell(checkup.bloodPressure.toString(), fixedHeight = 35f))
            table.addCell(createCell(checkup.height.toString(), fixedHeight = 35f))
            table.addCell(createCell(checkup.weight.toString(), fixedHeight = 35f))
            table.addCell(createCell(checkup.gravidaPara, fixedHeight = 35f))
            table.addCell(createCell(formattedLastMenstrualPeriod ?: "", fixedHeight = 35f))
            table.addCell(createCell(checkupReport.nutritionalStatus, fixedHeight = 35f))
            table.addCell(createCell(checkupReport.ageOfGestation, fixedHeight = 35f))
            table.addCell(createCell(formattedExpectedDueDate ?: "", fixedHeight = 35f))
        }

        document.add(table)
        addPageNumbers(document, writer)
        document.close()

        try {
            writer.close()
        } catch (ex: Exception) {
            onError(ex)
        } finally {
            onFinish(file)
        }
    } catch (e: Exception) {
        onError(e)
    }
}

fun openFile(context: Context, file: File) {
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY
    }
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "No PDF viewer installed", Toast.LENGTH_SHORT).show()
    }
}

fun createCell(content: String, fixedHeight: Float = 20f): PdfPCell {
    val phrase = Phrase(content)
    return PdfPCell(phrase).apply {
        horizontalAlignment = PdfPCell.ALIGN_CENTER
        verticalAlignment = PdfPCell.ALIGN_MIDDLE
        this.fixedHeight = fixedHeight
    }
}

fun createTable(columnCount: Int, columnWidths: FloatArray): PdfPTable {
    return PdfPTable(columnCount).apply {
        setWidths(columnWidths)
        widthPercentage = 100f
    }
}

fun addLineSpace(document: Document, lines: Int) {
    for (i in 0 until lines) {
        document.add(Paragraph(" "))
    }
}

fun createFile(context: Context): File {
    val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
    if (!directory?.exists()!!) {
        directory.mkdirs()
    }
    val fileName = "Maternal Care.pdf"
    val file = File(directory, fileName)
    if (file.exists()) {
        file.delete()
    }
    return file
}

fun addPageNumbers(document: Document, writer: PdfWriter) {
    val totalPages = writer.pageNumber
    for (i in 1..totalPages) {
        val cb = writer.directContentUnder
        val pageSize = document.pageSize
        ColumnText.showTextAligned(
            cb, Element.ALIGN_CENTER,
            Phrase("Page $i of $totalPages", FontFactory.getFont(FontFactory.HELVETICA, 10f)),
            (pageSize.left + pageSize.right) / 2, pageSize.bottom + 20, 0f
        )
    }
}