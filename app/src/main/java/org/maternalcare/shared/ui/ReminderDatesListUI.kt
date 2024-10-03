package org.maternalcare.shared.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.maternalcare.modules.main.menu.ui.TextContainer
import org.maternalcare.modules.main.user.model.dto.UserCheckupDto
import java.text.SimpleDateFormat
import java.util.Locale

@Preview(showSystemUi = true)
@Composable
fun ReminderDatesPreview() {
    val sampleCheckupDtos = listOf(
        UserCheckupDto(dateOfCheckUp = "2023-09-20T14:00:00.000Z"),
    )
    ReminderCheckupListUI(
        onDismiss = {},
        checkupDto = sampleCheckupDtos
    )
}


@Composable
fun ReminderCheckupListUI (
    onDismiss : () -> Unit,
    checkupDto: List<UserCheckupDto>
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth(0.89f)
                .heightIn(min = 200.dp, max = 430.dp)
                .border(
                    4.dp,
                    color = Color(0xFF6650a4),
                    shape = RoundedCornerShape(15.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                val displayText = listOf("Reminder", "Upcoming scheduled prenatal check-up for Pregnant Women on")
                displayText.forEach { text ->
                    TextContainer(text
                    = text)
                }

                val uniqueCheckupDetails = checkupDto
                    .map { formatListDates(it.dateOfCheckUp) }
                    .toSet()
                    .mapIndexed { index, date -> index + 1 to date }

                LazyColumn (
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .heightIn(min = 120.dp, max = 210.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(uniqueCheckupDetails.toList()) { (num, dates) ->
                        Text(
                            text = "$num. $dates",
                            textAlign = TextAlign.Justify,
                            fontSize = 16.sp,
                            color = Color(0xFF6650a4),
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Button(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6650a4),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .height(32.dp)
                    ) {
                        Text(
                            text = "Close",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}

fun formatListDates(dateString: String?): String {
    if (dateString.isNullOrBlank()) {
        return "No Date Available"
    }

    return try {
        val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date = isoFormatter.parse(dateString) ?: throw Exception("ISO format error")

        val displayFormatter = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        displayFormatter.format(date)
    } catch (e: Exception) {
        try {
            val simpleFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = simpleFormatter.parse(dateString) ?: throw Exception("Simple date format error")

            val displayFormatter = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            displayFormatter.format(date)
        } catch (e: Exception) {
            "Invalid Date"
        }
    }
}
