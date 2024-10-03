import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Environment
import android.os.StrictMode
import android.widget.Toast
import androidx.core.net.toUri
import com.itextpdf.text.Paragraph
import org.maternalcare.modules.main.user.model.dto.UserDto
import java.io.File
import com.itextpdf.text.Document
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.PageSize
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import java.io.FileOutputStream
import com.itextpdf.text.Element

fun exportToPDF(
    data: List<UserDto>,
    onFinish: (file: File) -> Unit,
    onError: (Exception) -> Unit
) {
    val file = createFile()
    val document = createDocument()

    try {
        setupPdfWriter(document, file)

        document.add(Paragraph("Maternal Care").apply {
            alignment = Element.ALIGN_CENTER
        })
        addLineSpace(document, 1)

        val columnWidths = floatArrayOf(0.2f, 1f, 1f, 1f)
        val table = createTable(4, columnWidths)

        val tableHeader = listOf("No", "First Name", "Middle Name", "Last Name")
        tableHeader.forEach {
            val cell = createCell(it, Element.ALIGN_LEFT)
            table.addCell(cell)
        }

        data.forEachIndexed { index, user ->
            table.addCell(createCell((index + 1).toString()))
            table.addCell(createCell(user.firstName ?: ""))
            table.addCell(createCell(user.middleName ?: ""))
            table.addCell(createCell(user.lastName ?: ""))
        }

        document.add(table)
        document.close()

        onFinish(file)
    } catch (e: Exception) {
        onError(e)
    }
}

fun createCell(content: String, alignment: Int = Element.ALIGN_LEFT): PdfPCell {
    val cell = PdfPCell(Paragraph(content))
    cell.horizontalAlignment = alignment
    return cell
}
fun addLineSpace(document: Document, lines: Int) {
    repeat(lines) {
        document.add(Paragraph(" "))
    }
}

fun createTable(numColumns: Int, columnWidths: FloatArray): PdfPTable {
    val table = PdfPTable(numColumns)
    table.setWidths(columnWidths)
    table.widthPercentage = 100f
    return table
}

fun openFile(context: android.content.Context, file: File) {
    val pdfUri = file.toUri()
    val builder = StrictMode.VmPolicy.Builder()
    StrictMode.setVmPolicy(builder.build())
    builder.detectFileUriExposure()

    val pdfIntent = Intent(Intent.ACTION_VIEW)
    pdfIntent.setDataAndType(pdfUri, "application/pdf")
    pdfIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

    try {
        context.startActivity(pdfIntent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "Can't read PDF file", Toast.LENGTH_SHORT).show()
    }
}

private fun createFile(): File {
    val title = "Maternal Care.pdf"
    val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val file = File(path, title)
    if (!file.exists()) file.createNewFile()
    return file
}

private fun createDocument(): Document {
    val document = Document()
    document.setMargins(24f, 24f, 32f, 32f)
    document.pageSize = PageSize.A4
    return document
}

fun setupPdfWriter(document: Document, file: File) {
    val pdfWriter = PdfWriter.getInstance(document, FileOutputStream(file))
    pdfWriter.setFullCompression()
    document.open()
}