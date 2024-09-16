package org.maternalcare.modules.main.user.ui

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.user.model.dto.UserDto
import org.maternalcare.shared.ext.hashPassword
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Preview(showSystemUi = true)
@Composable
fun UserFormPrev() {
    UserForm(onSubmit = { _ ->}, navController = rememberNavController() )
}

@Composable
fun UserForm(
    title : String = "Create Account",
    userDto: UserDto? = null,
    onSubmit: (UserDto) -> Unit,
    navController: NavController,
    includePassword: Boolean = true,
    isInitialOptionShow: Boolean = true
) {
    val listOfLabel = if (includePassword) {
        listOf("First Name", "Middle Name", "Last Name", "Email", "Address", "Mobile Number", "Date Of Birth", "Password")
    } else {
        listOf("First Name", "Middle Name", "Last Name", "Email", "Address", "Mobile Number", "Date Of Birth")
    }
    val userId by remember { mutableStateOf(userDto?.id ?: "") }
    var radioError by remember { mutableStateOf(false) }

    val statesValue = remember {
        listOfLabel.associateWith {
            mutableStateOf(
                when (it) {
                    "First Name" -> userDto?.firstName ?: ""
                    "Middle Name" -> userDto?.middleName ?: ""
                    "Last Name" -> userDto?.lastName ?: ""
                    "Email" -> userDto?.email ?: ""
                    "Address" -> userDto?.address ?: ""
                    "Mobile Number" -> userDto?.mobileNumber ?: ""
                    "Date Of Birth" -> userDto?.dateOfBirth ?: ""
                    "Password" -> userDto?.password ?: ""
                    else -> ""
                }
            )
        }
    }

    var selectedOption by remember { mutableStateOf(
        when {
            userDto?.isSuperAdmin == true -> "SuperAdmin"
            userDto?.isAdmin == true -> "Admin"
            userDto?.isResidence == true -> "Residence"
            else -> ""
        }
    )}

    var isActive by remember { mutableStateOf(true) }
    var isButtonEnabled by remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title,
            fontFamily = FontFamily.Serif,
            fontSize = 24.sp,
            modifier = Modifier
                .offset(y = (-6).dp)
        )

        val errors = remember {
            listOfLabel.associateWith { mutableStateOf("") }
        }
        ContainerLabelValue(statesValue, errors = errors)

        if (isInitialOptionShow) {
            val radioErrorMessage = if (radioError) "Please select an option" else ""
            FormRadioButton(
                selectedOption = selectedOption,
                onOptionSelected = { option ->
                    selectedOption = option
                    radioError = false
                },
                errorMsg = radioErrorMessage
            )
        }

        SwitchButton(isActiveState = isActive, onCheckedChange = { isActive = it } ,scale = 0.7f, switchText = "Active")

        ButtonSubmitData(
            statesValue = statesValue,
            userId = userId,
            selectedOption = selectedOption,
            isActiveState = isActive,
            onSubmit = {
                isButtonEnabled = false
                val hasError = validateForm(errors, statesValue)

                if (selectedOption.isEmpty()) {
                    radioError = true
                    isButtonEnabled = true
                }

                if (hasError || radioError) {
                    isButtonEnabled = true
                } else {
                    onSubmit(it)
                }
            },
            errors = errors,
            isEnableSubmit = isButtonEnabled,
        )

        Spacer(modifier = Modifier.padding(top = 4.dp))
        if (userDto == null) {
            TextButton(onClick = { navController.navigate(MainNav.User) }) {
                Text(text = "Cancel",
                    modifier = Modifier,
                    fontSize = 17.sp
                )
            }
        }
    }
}

@Composable
fun ContainerLabelValue(statesValue: Map<String, MutableState<String>>, errors: Map<String, MutableState<String>>) {
    statesValue.forEach { (label, state) ->
        if (label == "Date Of Birth") {
            DatePickerField(
                label = label,
                dateValue = state.value,
                onDateChange = { newValue ->
                    state.value = newValue
                    errors[label]?.value = if (newValue.isEmpty()) "This field is required" else ""
                },
                isError = errors[label]?.value?.isNotEmpty() == true,
                onErrorChange = { hasError ->
                    errors[label]?.value = if (hasError) "This field is required" else ""
                },
                errorMessage = errors[label]?.value ?: ""
            )
        }else{
            TextFieldContainer(
                textFieldLabel = label,
                textFieldValue = state.value,
                onValueChange = { newValue -> state.value = newValue },
                isError = errors[label]?.value?.isNotEmpty() == true,
                onErrorChange = { hasError ->
                    errors[label]?.value = if (hasError) "This field is required" else ""
                },
                errorMessage = errors[label]?.value ?: ""
            )
        }
    }
}

@Composable
fun TextFieldContainer(
    textFieldLabel: String,
    textFieldValue: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    onErrorChange: (Boolean) -> Unit,
    errorMessage: String = ""
) {
    var isPasswordField = textFieldLabel == "Password"
    var isPasswordVisible by remember { mutableStateOf(false) }
    val isPhoneNumberField = textFieldLabel == "Mobile Number"

    val colors = if (isError) {
        OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.Red,
        )
    } else {
        OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent
        )
    }

    if (textFieldLabel == "Date Of Birth") {

        DatePickerField(
            label = textFieldLabel,
            dateValue = textFieldValue,
            onDateChange = onValueChange,
            isError = isError,
            onErrorChange = onErrorChange,
            errorMessage = "Invalid date format"
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
                        if (isPhoneNumberField) {
                            if (it.all { char -> char.isDigit() }) {
                                onValueChange(it)
                                onErrorChange(false)
                            } else {
                                onErrorChange(true)
                            }
                        } else {
                            onValueChange(it)
                            onErrorChange(it.isEmpty())
                        }
                    },
                    placeholder = if (!isError) {
                        { Text("Enter value", color = Color.Black) }
                    } else null,
                    label = if (isError) {
                        {
                            Text(
                                text = errorMessage,
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.offset(y = (-3).dp)
                            )
                        }
                    } else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    visualTransformation = if (isPasswordField && !isPasswordVisible) {
                        PasswordVisualTransformation()
                    } else {
                        VisualTransformation.None
                    },
                    trailingIcon = {
                        if (isPasswordField) {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    painter = painterResource(
                                        id = if (isPasswordVisible) R.drawable.visibilityon else R.drawable.visibility_off
                                    ),
                                    contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                                )
                            }
                        }
                    },
                    colors = colors,
                    isError = isError
                )
            }
        }
    }
}

@Composable
fun FormRadioButton(
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    errorMsg : String
) {
    val activation = when (selectedOption) {
        "Residence" -> listOf("Residence")
        "SuperAdmin", "Admin" -> listOf("SuperAdmin", "Admin")
        else -> listOf("Residence")
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (errorMsg.isNotEmpty()) {
            Text(
                text = errorMsg,
                color = Color.Red,
                fontSize = 11.sp,
            )
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        activation.forEach { text ->
            Row(
                Modifier
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            onOptionSelected(text)
                        }
                    )
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = {
                        onOptionSelected(text)
                    },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Blue,
                        unselectedColor = Color.Gray
                    ),
                    modifier = Modifier
                        .size(18.dp)
                        .padding(10.dp)
                )
                Text(
                    text = text,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp, top = 3.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Composable
fun SwitchButton(isActiveState: Boolean,  onCheckedChange: (Boolean) -> Unit, scale: Float, switchText : String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .background(Color.White)
    ){
        Switch(
            checked = isActiveState,
            onCheckedChange = {
                onCheckedChange(it)
            },
            modifier = Modifier
                .scale(scale)
                .padding(0.dp),
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF6650a4),
                uncheckedThumbColor = Color.Gray,
                checkedTrackColor = Color(0xFF6650a4).copy(alpha = 0.4f),
                uncheckedTrackColor = Color.LightGray
            ),
            thumbContent = if (isActiveState) {
                {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                }
            } else {
                null
            }
        )
        Text(text = switchText,
            fontSize = 16.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = 53.dp)
        )
    }
}

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    label: String, dateValue: String,
    onDateChange: (String) -> Unit,
    isError: Boolean,
    onErrorChange: (Boolean) -> Unit,
    errorMessage: String = ""
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
                onErrorChange(false)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }
    val displayFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val displayDate = try {
        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(dateValue)
        date?.let { displayFormat.format(it) } ?: "Select Date"
    } catch (e: Exception) {
        "Select Date"
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

            Spacer(modifier = Modifier.width(18.dp))

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
                    if (isError) {
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            modifier = Modifier.padding(top = 4.dp),
                            fontSize = 12.sp
                        )
                    }else {
                        Text(
                            text = displayDate,
                            fontFamily = FontFamily.SansSerif,
                            modifier = Modifier
                                .padding(10.dp)
                                .align(Alignment.CenterStart),
                            fontSize = 17.sp,
                            color = Color.Black
                        )
                    }
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

@Composable
fun ButtonSubmitData(
    statesValue: Map<String, MutableState<String>>,
    selectedOption: String,
    isActiveState: Boolean,
    userId: String,
    onSubmit: (UserDto) -> Unit,
    errors: Map<String, MutableState<String>>,
    isEnableSubmit: Boolean,
) {
    Button(
        onClick = {

            val hasError = validateForm(errors, statesValue)
            if (!hasError) {
                val userDto = UserDto(
                    firstName = statesValue["First Name"]?.value ?: "",
                    middleName = statesValue["Middle Name"]?.value ?: "",
                    lastName = statesValue["Last Name"]?.value ?: "",
                    email = statesValue["Email"]?.value ?: "",
                    address = statesValue["Address"]?.value ?: "",
                    mobileNumber = statesValue["Mobile Number"]?.value ?: "",
                    dateOfBirth = statesValue["Date Of Birth"]?.value ?: "",
                    isSuperAdmin = selectedOption == "SuperAdmin",
                    isAdmin = selectedOption == "Admin",
                    isResidence = selectedOption == "Residence",
                    isActive = isActiveState
                )

                val password = statesValue["Password"]?.value
                if (userId.isEmpty()) {
                    userDto.password = password?.hashPassword() ?: ""
                } else {
                    userDto.id = userId
                    userDto.password = password ?: ""
                }

                // Invoke callback listener
                onSubmit(userDto)
            }
        },
        enabled = isEnableSubmit,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF6650a4),
            contentColor = Color(0xFFFFFFFF)
        ),
    ) {
        Text("Submit", fontSize = 18.sp)
    }
}

fun validateForm(
    errors: Map<String,MutableState<String>>,
    statesValue: Map<String,MutableState<String>>,
): Boolean {
    var hasError = false
    val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    statesValue.forEach { (label, state) ->
        when (label) {
            "First Name", "Last Name", "Middle Name" -> {
                val name = state.value
                if (name.isBlank()) {
                    errors[label]?.value = "Cannot be empty"
                    hasError = true
                } else if (name.any { it.isDigit() }) {
                    errors[label]?.value = "Cannot contain numbers"
                    hasError = true
                } else if (name.contains(" ")) {
                    errors[label]?.value = "Cannot contain spaces"
                    hasError = true
                } else if (!name.first().isUpperCase()) {
                    errors[label]?.value = "First letter uppercase"
                    hasError = true
                } else {
                    errors[label]?.value = ""
                }
            }
            "Email" -> {
                val email = state.value
                if (!emailPattern.matches(email)) {
                    errors[label]?.value = "Invalid email address"
                    hasError = true
                } else if (email != email.lowercase()) {
                    errors[label]?.value = "Email must be lowercase"
                    hasError = true
                } else {
                    errors[label]?.value = ""
                }
            }
            "Address" -> {
                val address = state.value
                if (address.isBlank()) {
                    errors[label]?.value = "Cannot be empty"
                    hasError = true
                } else if (address.length < 5) {
                    errors[label]?.value = "Address is too short"
                    hasError = true
                } else if (!address.matches(Regex("^[a-zA-Z0-9,. ]+$"))) {
                    errors[label]?.value = "Invalid address"
                    hasError = true
                } else {
                    errors[label]?.value = ""
                }
            }
            "Mobile Number" -> {
                if (state.value.length != 11) {
                    errors[label]?.value = "Must be 11 digits"
                    hasError = true
                } else if (!state.value.startsWith("09")) {
                    errors[label]?.value = "Must start with '09'"
                    hasError = true
                } else {
                    errors[label]?.value = ""
                }
            }
            "Password" -> {
                if (state.value.length < 6) {
                    errors[label]?.value = "Must be at least 6 characters"
                    hasError = true
                } else if (state.value.contains(" ")) {
                    errors[label]?.value = "Cannot contain spaces"
                    hasError = true
                } else {
                    errors[label]?.value = ""
                }
            }
            "Date Of Birth" -> {
                if (state.value.isEmpty()) {
                    errors[label]?.value = "Date of birth is required"
                    hasError = true
                } else {
                    errors[label]?.value = ""
                }
            }
        }
    }
    return hasError
}


