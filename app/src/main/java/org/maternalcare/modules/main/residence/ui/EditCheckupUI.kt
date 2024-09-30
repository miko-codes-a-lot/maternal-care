package org.maternalcare.modules.main.residence.ui

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.user.model.dto.UserCheckupDto
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Preview(showSystemUi = true)
@Composable
fun EditCheckupUIPreview() {
    val mockCheckups = UserCheckupDto(
        id = "66e7d37e209d993c619e73d9",
        userId = "66e7d37e209d993c619e73d9",
        checkup = 1,
        bloodPressure = 120.0,
        height = 175.0,
        weight = 70.0,
        dateOfCheckUp = "2024-09-17",
        lastMenstrualPeriod = "2024-08-01",
        scheduleOfNextCheckUp = "2024-10-01"
    )

    EditCheckupUI(
        navController = rememberNavController(),
        checkupNumber = 1,
        userDto = UserDto(),
        checkupUser = mockCheckups,
        currentUser = UserDto()
    )
}

@Composable
fun EditCheckupUI(
    navController: NavController,
    checkupNumber: Int,
    userDto: UserDto,
    checkupUser: UserCheckupDto? = null,
    currentUser: UserDto
) {
    val listOfLabel = listOf(
        "Blood Pressure", "Height", "Weight",
//        "Types of Vaccine",
        "Date of Check-up", "Last Menstrual Period", "Next Check-up"
    )
    val statesValue = remember {
        listOfLabel.associateWith {
            mutableStateOf(
                when (it) {
                    "Blood Pressure" -> checkupUser?.bloodPressure?.toString() ?: "0.0"
                    "Height" -> checkupUser?.height?.toString() ?: "0.0"
                    "Weight" -> checkupUser?.weight?.toString() ?: "0.0"
//                    "Types of Vaccine" -> checkupUser?.typeOfVaccine ?: ""
                    "Date of Check-up" -> checkupUser?.dateOfCheckUp ?: ""
                    "Last Menstrual Period" -> checkupUser?.lastMenstrualPeriod ?: ""
                    "Next Check-up" -> checkupUser?.scheduleOfNextCheckUp ?: ""
                    else -> ""
                }
            )
        }
    }
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ContainerValue(statesValue)
        Spacer(modifier = Modifier.height(8.dp))
        ButtonSaveEdit(
            userId = userDto.id!!,
            statesValue = statesValue,
            checkupNumber = checkupNumber,
            navController = navController,
            checkupUser = checkupUser ?: UserCheckupDto(),
            currentUser = currentUser
        )
    }
}

@Composable
fun ContainerValue(statesValue: Map<String, MutableState<String>>) {
    statesValue.forEach { (labels, states) ->
        if (labels == "Date Of Birth") {
            EditDatePickerField(
                label = labels,
                dateValue = states.value,
                onDateChange = { newValue -> states.value = newValue }
            )
        }else{
            TextFieldEditCheckUp(
                textFieldLabel = labels,
                textFieldValue = states.value,
                onValueChange = { newValue -> states.value = newValue },
            )
        }
    }
}

@Composable
fun TextFieldEditCheckUp(
    textFieldLabel: String,
    textFieldValue: String,
    onValueChange: (String) -> Unit
) {
    if (textFieldLabel == "Last Menstrual Period" ||
        textFieldLabel == "Date of Check-up" ||
        textFieldLabel == "Next Check-up"
    ) {
        EditDatePickerField(
            label = textFieldLabel,
            dateValue = textFieldValue,
            onDateChange = onValueChange,
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = textFieldLabel,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 17.sp
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(" : ", fontWeight = FontWeight.Bold)
                TextField(
                    value = textFieldValue,
                    onValueChange = {
                        onValueChange(it)
                    },
                    placeholder = {
                        Text("Enter value", color = Color.Black)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Black,
                        focusedBorderColor = Color.Black,
                        disabledBorderColor = Color.Gray,
                        errorBorderColor = Color.Red,
                        cursorColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                    )
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun ButtonSaveEdit(
    userId: String,
    statesValue: Map<String, MutableState<String>>,
    navController: NavController,
    checkupNumber: Int,
    currentUser: UserDto,
    checkupUser: UserCheckupDto
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            val checkUpDto = UserCheckupDto(
                id = checkupUser.id,
                userId = userId,
                bloodPressure = statesValue["Blood Pressure"]?.value?.toDoubleOrNull() ?: 0.0,
                height = statesValue["Height"]?.value?.toDoubleOrNull() ?: 0.0,
                weight = statesValue["Weight"]?.value?.toDoubleOrNull() ?: 0.0,
                typeOfVaccine = statesValue ["Types of Vaccine"]?.value ?: "",
                checkup = checkupNumber,
                lastMenstrualPeriod = statesValue["Last Menstrual Period"]?.value ?: "",
                dateOfCheckUp = statesValue["Date of Check-up"]?.value ?: "",
                scheduleOfNextCheckUp = statesValue["Next Check-up"]?.value ?: "",
                createdById = checkupUser.createdById ?: currentUser.id
            )

            scope.launch {
                try {
                    val result = userViewModel.upsertCheckUp(checkUpDto)
                    if (result.isSuccess) {
                        Log.d("CheckUpSave", "Saving CheckUpDto: $checkUpDto")

                        navController.navigate(MainNav.CheckupDetails(userId, checkupNumber)) {
                            popUpTo(MainNav.CheckupDetails(userId, checkupNumber)) {
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
            .padding(top = 27.dp)
            .height(54.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF6650a4),
            contentColor = Color.White
        )
    ) {
        Text(text = "Confirm", fontSize = 17.sp)
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun EditDatePickerField(
    label: String,
    dateValue: String,
    onDateChange: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
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

    val displayFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val displayDate = try {
        val dateISO = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(dateValue)
        dateISO?.let { displayFormat.format(it) } ?: throw Exception("ISO Parse failed")
    } catch (isoException: Exception) {
        try {
            val dateSimple = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateValue)
            dateSimple?.let { displayFormat.format(it) } ?: "Select Date"
        } catch (e: Exception) {
            "Select Date"
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .clickable {
                datePickerDialog.show()
            }
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                fontSize = 17.sp
            )

            Spacer(modifier = Modifier.width(10.dp))

            Icon(
                painter = painterResource(id = R.drawable.calendar_icon),
                contentDescription = "Calendar Icon",
                modifier = Modifier.size(24.dp)
            )

            Text(" : ", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.width(10.dp))

            Surface(
                modifier = Modifier
                    .clickable {
                        datePickerDialog.show()
                    }
                    .padding(4.dp)
                    .background(Color.Transparent),
            ) {
                Box(
                    modifier = Modifier
                        .width(202.dp)
                        .height(40.dp)
                        .background(Color.White)
                ){
                    Text(
                        text = displayDate,
                        fontFamily = FontFamily.SansSerif,
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.CenterStart),
                        fontSize = 17.sp,
                        color = Color.Black
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}