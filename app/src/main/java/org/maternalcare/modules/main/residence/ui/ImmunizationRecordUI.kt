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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.user.model.dto.UserBirthRecordDto
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.modules.main.user.model.dto.UserImmunizationDto
import org.maternalcare.modules.main.user.model.dto.UserTrimesterRecordDto
import org.maternalcare.modules.main.user.model.entity.UserTrimesterRecord
import org.maternalcare.modules.main.user.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Composable
fun ImmunizationRecordUI(
    navController: NavController,
    userDto: UserDto,
    userImmunization: UserImmunizationDto,
    currentUser: UserDto,
    pregnantRecord: UserBirthRecordDto,
) {
    val isAdminShow = currentUser.isAdmin
    val dateCreated = remember { mutableStateOf(userImmunization.createdAt ?: "") }
    val conditionStates = remember {
        mapOf(
            "1st Dose" to Pair(
                mutableStateOf(userImmunization.firstDoseGiven),
                mutableStateOf(userImmunization.firstDoseReturn)
            ),
            "2nd Dose" to Pair(
                mutableStateOf(userImmunization.secondDoseGiven),
                mutableStateOf(userImmunization.secondDoseReturn)
            ),
            "3rd Dose" to Pair(
                mutableStateOf(userImmunization.thirdDoseGiven),
                mutableStateOf(userImmunization.thirdDoseReturn)
            ),
            "4th Dose" to Pair(
                mutableStateOf(userImmunization.fourthDoseGiven),
                mutableStateOf(userImmunization.fourthDoseReturn)
            ),
            "5th Dose" to Pair(
                mutableStateOf(userImmunization.fifthDoseGiven),
                mutableStateOf(userImmunization.fifthDoseReturn)
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
            ImmunizationValue(
                conditionStates,
                isEditable = isAdminShow,
                currentUser = currentUser
            )
//            Spacer(modifier = Modifier.height(20.dp))

            if (isAdminShow) {
                DateCreatedValue(
                    label = "Date Created",
                    dateValue = dateCreated.value,
                    onDateChange = { newDate -> dateCreated.value = newDate }
                )
                Spacer(modifier = Modifier.height(25.dp))
                ButtonSaveRecord(
                    userId = userDto.id!!,
                    navController = navController,
                    conditionStates = conditionStates,
                    userImmunization = userImmunization,
                    currentUser = currentUser,
                    pregnantRecordId = pregnantRecord.id!!,
                    createdAtState = dateCreated
                )
            }
        }
    }
}

@Composable
fun ImmunizationValue(
    statesValue: Map<String, Pair<MutableState<String?>, MutableState<String?>>>,
    isEditable: Boolean,
    currentUser: UserDto
) {
    val isSuperAdmin = currentUser.isSuperAdmin
    val isResidence = currentUser.isResidence

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
                    dateValue = resolveDateValue(givenDateState.value, isSuperAdmin, isResidence),
                    onDateChange = { newDate -> givenDateState.value = newDate },
                    isEditable = isEditable

                )

                Spacer(modifier = Modifier.width(15.dp))

                DatePickerBox(
                    dateValue = resolveDateValue(returnDateState.value, isSuperAdmin, isResidence),
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

fun resolveDateValue(date: String?, isSuperAdmin: Boolean, isResidence: Boolean): String {
    return when {
        date.isNullOrEmpty() -> {
            if (isSuperAdmin) "Pending"
            else if (isResidence) "Not available"
            else "Select Date"
        }
        else -> date
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
    val displayDate = when (dateValue) {
        "Pending" -> "Pending"
        "Not available" -> "Not available"
        else -> try {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(dateValue)?.let {
                displayFormat.format(it)
            } ?: "Select Date"
        } catch (e: Exception) {
            "Select Date"
        }
    }
    val textColor = if (dateValue.isNotEmpty() && dateValue != "Select Date" && dateValue != "Pending" && dateValue != "Not available") {
        Color(0xFF6650a4)
    } else {
        Color.Black
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
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
                color = textColor,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.W500
            )
        }
    }
}

@Composable
fun ButtonSaveRecord(
    userId: String,
    currentUser: UserDto,
    navController: NavController,
    conditionStates: Map<String, Pair<MutableState<String?>, MutableState<String?>>>,
    userImmunization: UserImmunizationDto,
    pregnantRecordId: String,
    createdAtState: MutableState<String>
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Button(
        onClick = {
            val userImmunizationRecord  = UserImmunizationDto(
                id = userImmunization.id,
                userId = userId,
                pregnantRecordId = pregnantRecordId,
                firstDoseGiven = conditionStates["1st Dose"]?.first?.value,
                firstDoseReturn = conditionStates["1st Dose"]?.second?.value,
                secondDoseGiven = conditionStates["2nd Dose"]?.first?.value,
                secondDoseReturn = conditionStates["2nd Dose"]?.second?.value,
                thirdDoseGiven = conditionStates["3rd Dose"]?.first?.value,
                thirdDoseReturn = conditionStates["3rd Dose"]?.second?.value,
                fourthDoseGiven = conditionStates["4th Dose"]?.first?.value,
                fourthDoseReturn = conditionStates["4th Dose"]?.second?.value,
                fifthDoseGiven = conditionStates["5th Dose"]?.first?.value,
                fifthDoseReturn = conditionStates["5th Dose"]?.second?.value,
                createdById = currentUser.id ?: userImmunization.createdById,
                createdAt = createdAtState.value
            )
            scope.launch {
                try {
                    val result = userViewModel.upsertImmunization(userImmunizationRecord)
                    if (result.isSuccess) {
                        navController.popBackStack()
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
        Text(text = "Confirm", fontSize = 17.sp, fontFamily = FontFamily.SansSerif)
    }
}

@Composable
fun DateCreatedValue(
    label: String,
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
                fontSize = 17.sp,
                modifier = Modifier.padding(start = 20.dp)
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
                            .padding(start = 20.dp)
                            .align(Alignment.CenterStart),
                        fontSize = 17.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = Color.Black
    )
}