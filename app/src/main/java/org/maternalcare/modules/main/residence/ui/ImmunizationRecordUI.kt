package org.maternalcare.modules.main.residence.ui

import android.app.DatePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import kotlinx.coroutines.launch
import org.maternalcare.modules.main.user.model.dto.UserConditionDto
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.model.dto.UserImmunizationDto
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Preview(showSystemUi = true)
@Composable
fun ImmunizationRecordUIPreview() {
    ImmunizationRecordUI(
//        userDto = UserDto(),
        userImmunization = UserImmunizationDto(),
//        currentUser = UserDto()
    )
}

@Composable
fun ImmunizationRecordUI(
//    userDto: UserDto,
    userImmunization: UserImmunizationDto? = null,
//    currentUser: UserDto
) {
    val conditionStates = mapOf(
        "1st Dose" to Pair(
            remember { mutableStateOf(userImmunization?.firstDoseGiven ?: "") },
            remember { mutableStateOf(userImmunization?.firstDoseReturn ?: "") }
        ),
        "2nd Dose" to Pair(
            remember { mutableStateOf(userImmunization?.secondDoseGiven ?: "") },
            remember { mutableStateOf(userImmunization?.secondDoseReturn ?: "") }
        ),
        "3rd Dose" to Pair(
            remember { mutableStateOf(userImmunization?.thirdDoseGiven ?: "") },
            remember { mutableStateOf(userImmunization?.thirdDoseReturn ?: "") }
        ),
        "4th Dose" to Pair(
            remember { mutableStateOf(userImmunization?.fourthDoseGiven ?: "") },
            remember { mutableStateOf(userImmunization?.fourthDoseReturn ?: "") }
        ),
        "5th Dose" to Pair(
            remember { mutableStateOf(userImmunization?.fifthDoseGiven ?: "") },
            remember { mutableStateOf(userImmunization?.fifthDoseReturn ?: "") }
        )
    )
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
            ImmunizationValue(conditionStates)
            Spacer(modifier = Modifier.height(20.dp))
            ButtonSaveRecord(

            )
        }
    }
}

@Composable
fun ImmunizationValue(statesValue: Map<String, Pair<MutableState<String>, MutableState<String>>>) {
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
                    onDateChange = { newDate -> givenDateState.value = newDate }
                )

                Spacer(modifier = Modifier.width(15.dp))

                DatePickerBox(
                    dateValue = returnDateState.value,
                    onDateChange = { newDate -> returnDateState.value = newDate }
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
    onDateChange: (String) -> Unit
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
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(dateValue)?.let {
            displayFormat.format(it)
        } ?: "Select Date"
    } catch (e: Exception) {
        "Select Date"
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { datePickerDialog.show() }
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
//    userId: String,
//    currentUser: UserDto,
) {
//    val userViewModel: UserViewModel = hiltViewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            val userConditionStatus  = UserConditionDto(
//                id = userCondition.id,
//                userId = userId,

//                createdById = userCondition.createdById ?: currentUser.id
            )

            scope.launch {
                try {
//                    val result = userViewModel.upsertCheckUp(userCondition)
//                    if (result.isSuccess) {
                    Log.d("CheckUpSave", "Saving CheckUpDto: $userConditionStatus")

//                        navController.navigate(MainNav.CheckupDetails(userId, checkupNumber)) {
//                            popUpTo(MainNav.CheckupDetails(userId, checkupNumber)) {
//                                inclusive = true
//                            }
//                        }
//                    } else {
//                        Log.e("saving", "Error: ${result.exceptionOrNull()}")
//                    }
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