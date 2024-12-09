package org.maternalcare.shared.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import org.maternalcare.modules.main.user.model.dto.UserCheckupDto
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.shared.ext.assessBloodPressure
import org.maternalcare.shared.ext.calculateAgeOfGestation
import org.maternalcare.shared.ext.determineBMICategory
import org.maternalcare.shared.ext.expectedDueDate
import java.text.SimpleDateFormat
import java.util.Locale

@Preview(showSystemUi = true)
@Composable
fun ReminderCheckupUIPreview() {
    ReminderCheckupUI(onDismiss = {},checkup = UserCheckupDto(), userDto = UserDto())
}

@Composable
fun ReminderCheckupUI (
    onDismiss : () -> Unit,
    checkup: UserCheckupDto,
    userDto: UserDto
){
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
                .fillMaxWidth(0.95f)
                .padding(16.dp)
                .heightIn(min = 100.dp, max = 600.dp)
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
                CheckUpValueContainer(checkup = checkup, userDto = userDto)
                Row(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
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
                            .height(35.dp)
                    ) {
                        Text(
                            text = "Close",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CheckUpValueContainer (userDto: UserDto, checkup: UserCheckupDto) {
    val bmiCategory = checkup.determineBMICategory()
    val bloodPressureStatus = checkup.assessBloodPressure()

    val labelValueMap = mutableMapOf(
        "Name" to userDto.firstName +" "+ userDto.middleName +" "+ userDto.lastName,
        "Blood Pressure" to bloodPressureStatus,
        "Age Of Gestation" to "${checkup.calculateAgeOfGestation()} weeks",
        "Nutritional Status" to  "%s".format(bmiCategory),
        "Expected Due Date" to checkup.expectedDueDate(),
    )
    if (checkup.checkup < 3) {
        labelValueMap["Next Check-up"] = formatDate(checkup.scheduleOfNextCheckUp)
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        labelValueMap.forEach { (label, value) ->
            Text(
                text = "$label : $value",
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
                color =Color(0xFF6650a4),
                fontFamily = (FontFamily.SansSerif),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

fun formatDate(dateString: String?): String {
    if (dateString.isNullOrBlank()) {
        return "No Date Available"
    }
    return try {
        val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val displayFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = isoFormatter.parse(dateString) ?: throw Exception("ISO format error")
        displayFormatter.format(date)
    } catch (e: Exception) {
        try {
            val simpleFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val displayFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val date = simpleFormatter.parse(dateString) ?: throw Exception("Simple date format error")
            displayFormatter.format(date)
        } catch (e: Exception) {
            "Invalid Date"
        }
    }
}
