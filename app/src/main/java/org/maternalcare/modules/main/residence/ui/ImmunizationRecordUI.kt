package org.maternalcare.modules.main.residence.ui

import android.app.DatePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.model.dto.UserImmunizationDto
import org.maternalcare.modules.main.user.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Preview(showSystemUi = true)
@Composable
fun ImmunizationRecordUIPreview() {
    ImmunizationRecordUI(
        navController = rememberNavController(),
        userDto = UserDto(),
        userImmunization = UserImmunizationDto(),
        currentUser = UserDto()
    )
}

@Composable
fun ImmunizationRecordUI(
    navController: NavController,
    userDto: UserDto,
    userImmunization: UserImmunizationDto,
    currentUser: UserDto
) {
    val isStatusVisible = currentUser.isSuperAdmin || currentUser.isResidence

    val conditionStates = if (isStatusVisible) {
        mapOf(
            "1st Dose" to Pair(
                remember { mutableStateOf("11-05-2024") },
                remember { mutableStateOf("11-12-2024") }
            ),
            "2nd Dose" to Pair(
                remember { mutableStateOf("11-15-2024") },
                remember { mutableStateOf("11-20-2024") }
            ),
            "3rd Dose" to Pair(
                remember { mutableStateOf("11-26-2024") },
                remember { mutableStateOf("12-01-2024") }
            ),
            "4th Dose" to Pair(
                remember { mutableStateOf("12-08-2024") },
                remember { mutableStateOf("12-16-2024") }
            ),
            "5th Dose" to Pair(
                remember { mutableStateOf("12-24-2024") },
                remember { mutableStateOf("01-03-2024") }
            )
        )
    } else {
        mapOf(
            "1st Dose" to Pair(
                remember { mutableStateOf(userImmunization.firstDoseGiven) },
                remember { mutableStateOf(userImmunization.firstDoseReturn) }
            ),
            "2nd Dose" to Pair(
                remember { mutableStateOf(userImmunization.secondDoseGiven) },
                remember { mutableStateOf(userImmunization.secondDoseReturn) }
            ),
            "3rd Dose" to Pair(
                remember { mutableStateOf(userImmunization.thirdDoseGiven) },
                remember { mutableStateOf(userImmunization.thirdDoseReturn) }
            ),
            "4th Dose" to Pair(
                remember { mutableStateOf(userImmunization.fourthDoseGiven) },
                remember { mutableStateOf(userImmunization.fourthDoseReturn) }
            ),
            "5th Dose" to Pair(
                remember { mutableStateOf(userImmunization.fifthDoseGiven) },
                remember { mutableStateOf(userImmunization.fifthDoseReturn) }
            )
        )
    }

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Immunization Record",
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 19.sp,
                    color = Color.Black
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tetanus Vaccine",
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                )
                {
                    Text(
                        text = "Given Date",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(30.dp))
                    Text(
                        text = "Return Date",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }
//            ImmunizationValue(conditionStates)
            ImmunizationValue(conditionStates, isEditable = !isStatusVisible)
            Spacer(modifier = Modifier.height(20.dp))

            if (!isStatusVisible) {
                ButtonSaveRecord(
                    userId = userDto.id!!,
                    navController = navController,
                    conditionStates = conditionStates,
                    userImmunization = userImmunization,
                    currentUser = currentUser
                )
            }
        }
    }
}

@Composable
fun ImmunizationValue(statesValue: Map<String, Pair<MutableState<String>, MutableState<String>>>, isEditable: Boolean) {
    statesValue.forEach { (label, dateStates) ->
        val (givenDateState, returnDateState) = dateStates
        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$label :",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
                    .padding(start = 20.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                DatePickerBox(
                    dateValue = givenDateState.value,
                    onDateChange = { newDate -> givenDateState.value = newDate },
                    isEditable = isEditable
                )

                Spacer(modifier = Modifier.width(15.dp))

                DatePickerBox(
                    dateValue = returnDateState.value,
                    onDateChange = { newDate -> returnDateState.value = newDate },
                    isEditable = isEditable
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 1.dp,
            color = Color.Black
        )
    }
}

@Composable
fun DatePickerBox(
    dateValue: String,
    onDateChange: (String) -> Unit,
    isEditable: Boolean
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = remember {
        DatePickerDialog(
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

    val displayFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val displayDate = try {
        if (Regex("\\d{2}-\\d{2}-\\d{4}").matches(dateValue)) {
            dateValue
        } else {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(dateValue)?.let {
                displayFormat.format(it)
            } ?: "Select Date"
        }
    } catch (e: Exception) {
        "Select Date"
    }
//    val displayDate = try {
//        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(dateValue)?.let {
//            displayFormat.format(it)
//        } ?: "Select Date"
//    } catch (e: Exception) {
//        "Select Date"
//    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.clickable { datePickerDialog.show() }
        modifier = Modifier
            .clickable(enabled = isEditable) { datePickerDialog.show() }
    ) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp)
                .height(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = displayDate,
                fontSize = 16.sp,
                color = Color.Black,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}


@Composable
fun ButtonSaveRecord(
    userId: String,
    currentUser: UserDto,
    navController: NavController,
    conditionStates: Map<String, Pair<MutableState<String>, MutableState<String>>>,
    userImmunization: UserImmunizationDto,
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Button(
        onClick = {

            Log.d("UserID", "User ID is: $userId")
            Log.d("CurrentUserID", "Current User ID is: ${currentUser.id}")

            val userImmunizationRecord  = UserImmunizationDto(
                id = userImmunization.id,
                userId = userId,
                firstDoseGiven = conditionStates["1st Dose"]?.first?.value ?: "",
                firstDoseReturn = conditionStates["1st Dose"]?.second?.value ?: "",
                secondDoseGiven = conditionStates["2nd Dose"]?.first?.value ?: "",
                secondDoseReturn = conditionStates["2nd Dose"]?.second?.value ?: "",
                thirdDoseGiven = conditionStates["3rd Dose"]?.first?.value ?: "",
                thirdDoseReturn = conditionStates["3rd Dose"]?.second?.value ?: "",
                fourthDoseGiven = conditionStates["4th Dose"]?.first?.value ?: "",
                fourthDoseReturn = conditionStates["4th Dose"]?.second?.value ?: "",
                fifthDoseGiven = conditionStates["5th Dose"]?.first?.value ?: "",
                fifthDoseReturn = conditionStates["5th Dose"]?.second?.value ?: "",
                createdById = currentUser.id ?: userImmunization.createdById
            )

            scope.launch {
                try {
                    val result = userViewModel.upsertImmunization(userImmunizationRecord)
                    if (result.isSuccess) {
                    Log.d("CheckUpSave", "Saving CheckUpDto: $userImmunizationRecord")

                        navController.navigate(MainNav.ImmunizationRecord(userId)) {
                            popUpTo(MainNav.ImmunizationRecord(userId)) {
                                inclusive = true
                            }
                        }
                    } else {
                        Log.e("saving", "Error: ${result.exceptionOrNull()}")
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Error updating data: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        },
        modifier = Modifier
            .width(360.dp)
            .height(54.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF6650a4),
            contentColor = Color.White
        )
    ) {
        Text(text = "Confirm", fontSize = 17.sp)
    }
}