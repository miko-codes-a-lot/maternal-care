package org.maternalcare.modules.main.residence.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.maternalcare.modules.main.user.model.dto.UserBirthRecordDto
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Preview(showSystemUi = true)
@Composable
fun CreateHealthRecordPreview() {
    CreateHealthRecordUI(
        userDto = UserDto(),
        rememberNavController(),
        pregnantRecord = UserBirthRecordDto()
    )
}

@Composable
fun CreateHealthRecordUI(
    userDto: UserDto,
    navController: NavController,
    pregnantRecord: UserBirthRecordDto?
) {
    val userViewModel: UserViewModel = hiltViewModel()
    var childNumber by remember { mutableStateOf(pregnantRecord?.childOrder?.takeIf { it != 0 }?.toString() ?: "") }
    var pregnancyOrder by remember { mutableStateOf(pregnantRecord?.pregnancyOrder?.takeIf { it != 0 }?.toString() ?: "") }
    var fillDate by remember { mutableStateOf(pregnantRecord?.fillDate ?: "") }
    var isSubmitClicked by remember { mutableStateOf(false) }
    var validationMessage by remember { mutableStateOf("") }
    val existingRecords = userViewModel.getHealthRecords(userDto.id!!)
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.White)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isSubmitClicked && validationMessage.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .align(Alignment.CenterHorizontally)
            ){
                Text(
                    text = validationMessage,
                    color = Color.Red,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

        }
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = childNumber,
            onValueChange = { newNumber ->
                if (newNumber.all { it.isDigit() }) {
                    childNumber = newNumber
                }
            },
            label = { Text("Which child is this? / Pang-ilang anak?", color = Color.Black, fontSize = 15.sp) },
            modifier = Modifier
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFF6650a4),
                focusedBorderColor = Color(0xFF6650a4),
                focusedTextColor = Color.Black,
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = pregnancyOrder,
            onValueChange = { newNumber ->
                if (newNumber.all { it.isDigit() }) {
                    pregnancyOrder = newNumber
                }
            },
            label = { Text("Pregnancy Order", color = Color.Black, fontSize = 15.sp) },
            modifier = Modifier
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFF6650a4),
                focusedBorderColor = Color(0xFF6650a4),
                focusedTextColor = Color.Black,
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        DateSelector(
            label = "Fill Date",
            dateValue = fillDate,
            onDateChange = { selectedDate ->
                fillDate = selectedDate
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        ButtonRecordSaved(
            userId = userDto.id!!,
            navController = navController,
            pregnantRecord = pregnantRecord!!,
            childNumber = childNumber.toIntOrNull() ?: 0,
            pregnancyOrder = pregnancyOrder.toIntOrNull() ?: 0,
            fillDate = fillDate,
            existingRecords = existingRecords,
            onSubmitClick = { isSubmitClicked = true },
            setValidationMessage = { validationMessage = it }
        )
    }
}

@Composable
fun DateSelector(
    label: String,
    dateValue: String,
    onDateChange: (String) -> Unit,
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                isoFormat.timeZone = TimeZone.getTimeZone("UTC")
                val dateISO = isoFormat.format(calendar.time)
                onDateChange(dateISO)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }
    val displayFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

    val displayDate = if (dateValue.isEmpty()) {
        displayFormat.format(calendar.time)
    } else {
        try {
            val date = isoFormat.parse(dateValue)
            date?.let { displayFormat.format(it) } ?: displayFormat.format(calendar.time)
        } catch (e: Exception) {
            displayFormat.format(calendar.time)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(
                    width = 1.dp,
                    color = Color(0xFF6650a4),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                )
                .background(Color.White)
                .clickable { datePickerDialog.show() }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = displayDate,
                fontFamily = FontFamily.SansSerif,
                fontSize = 16.sp,
                color = Color.Black,
            )
        }
    }

}

@Composable
fun ButtonRecordSaved(
    userId: String,
    navController: NavController,
    pregnantRecord: UserBirthRecordDto,
    childNumber: Int,
    pregnancyOrder: Int,
    fillDate: String,
    existingRecords: List<UserBirthRecordDto>,
    onSubmitClick: () -> Unit,
    setValidationMessage: (String) -> Unit
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Button(
        onClick = {
            onSubmitClick()
            val isDuplicate = existingRecords.any {
                (it.childOrder == childNumber && childNumber != 0) ||
                (it.pregnancyOrder == pregnancyOrder && pregnancyOrder != 0)
            }
            if (isDuplicate) {
                setValidationMessage("A record with this child or pregnancy number already exists.")
            } else{
                setValidationMessage("")
                val userPregnantRecord  = UserBirthRecordDto(
                    id = pregnantRecord.id,
                    pregnancyUserId = userId,
                    childOrder = childNumber,
                    pregnancyOrder = pregnancyOrder,
                    fillDate = fillDate
                )
                scope.launch {
                    try {
                        val result = userViewModel.upsertHealthRecord(userPregnantRecord)
                        if (result.isSuccess) {
                            navController.popBackStack()
                        } else {
                            Log.e("CreateHealthRerecordUI", "Error: ${result.exceptionOrNull()}")
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "Error saving health record data: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF6650a4))
            .height(55.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF6650a4),
            contentColor = Color(0xFFFFFFFF),
        )
    ) {
        Text(
            "Submit",
            fontSize = 16.sp,
            fontFamily = FontFamily.SansSerif,
            color = Color.White
        )
    }
}
