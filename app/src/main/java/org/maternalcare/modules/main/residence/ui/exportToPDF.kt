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
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import org.maternalcare.modules.main.user.model.dto.UserDto
import java.io.File
import java.io.FileOutputStream

fun exportToPDF(
    context: Context,
    data: List<UserDto>,
    onFinish: (file: File) -> Unit,
    onError: (Exception) -> Unit
) {
    val titleFont: Font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18f)
    try {
        val file = createFile(context)
        val document = Document(PageSize.A4)
        document.setMargins(24f, 24f, 32f, 32f)
        val pdfWriter = PdfWriter.getInstance(document, FileOutputStream(file))
        pdfWriter.setFullCompression()
        document.open()
        val titleParagraph = Paragraph("Residents Report", titleFont)
        titleParagraph.alignment = Element.ALIGN_CENTER
        document.add(titleParagraph)
        addLineSpace(document, 1)
        val columnWidths = floatArrayOf(0.2f, 1f, 1f, 1f)
        val table = createTable(4, columnWidths)
        val headers = listOf("No", "First Name", "Middle Name", "Last Name")

        headers.forEach {
            val cell = createCell(it)
            table.addCell(cell)
        }
        data.forEachIndexed { index, user ->
            table.addCell(createCell((index + 1).toString()))
            table.addCell(createCell(user.firstName))
            table.addCell(createCell(user.middleName ?: ""))
            table.addCell(createCell(user.lastName))
        }
        document.add(table)
        document.close()
        try {
            pdfWriter.close()
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

fun createCell(content: String): PdfPCell {
    val phrase = Phrase(content)
    return PdfPCell(phrase).apply {
        horizontalAlignment = PdfPCell.ALIGN_CENTER
        verticalAlignment = PdfPCell.ALIGN_MIDDLE
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