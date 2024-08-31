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
    navController: NavController
) {
    val listOfLabel = listOf(
        "First Name", "Middle Name", "Last Name", "Email", "Mobile Number",
        "Date Of Birth", "Password"
    )
    val statesValue = remember {
        listOfLabel.associateWith {
            mutableStateOf(
                when (it) {
                    "First Name" -> userDto?.firstName ?: ""
                    "Middle Name" -> userDto?.middleName ?: ""
                    "Last Name" -> userDto?.lastName ?: ""
                    "Email" -> userDto?.email ?: ""
                    "Mobile Number" -> userDto?.mobileNumber ?: ""
                    "Date Of Birth" -> userDto?.dateOfBirth ?: ""
                    "Password" -> userDto?.password ?: ""
                    else -> ""
                }
            )
        }
    }
    var selectedOption by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(true) }

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
                .padding(bottom = 3.dp, top = 7.dp)
        )
//        " ${userDto?.firstName ?: ""}"
        ContainerLabelValue(statesValue)

        Spacer(modifier = Modifier.padding(top = 10.dp))

        FormRadioButton(selectedOption = selectedOption, onOptionSelected =  { selectedOption = it } )

        SwitchButton(isActiveState = isActive, onCheckedChange = { isActive = it } ,scale = 0.7f, switchText = "Active")

        ButtonSubmitData(statesValue, selectedOption,  isActive, onSubmit = onSubmit)

        Spacer(modifier = Modifier.padding(top = 6.dp))

        TextButton(onClick = {  navController.navigate(MainNav.User) }) {
            Text(text = "Cancel",
                modifier = Modifier,
                fontSize = 17.sp
            )
        }
    }
}

@Composable
fun ContainerLabelValue(statesValue: Map<String, MutableState<String>>) {
    statesValue.forEach { (labels, states) ->
        TextFieldContainer(textFieldLabel = labels, textFieldValue = states.value) { newValues ->
            states.value = newValues
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldContainer(textFieldLabel: String, textFieldValue: String, onValueChange: (String) -> Unit) {
    var isPasswordField = textFieldLabel == "Password"
    var isPasswordVisible by remember { mutableStateOf(false) }
    val isPhoneNumberField = textFieldLabel == "Mobile Number"

    if (textFieldLabel == "Date Of Birth") {
        DatePickerField(label = textFieldLabel, dateValue = textFieldValue, onDateChange = onValueChange)
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
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
               Spacer(modifier = Modifier.width(8.dp))
               Text(" : ", fontWeight = FontWeight.Bold)
               Spacer(modifier = Modifier.width(8.dp))
               TextField(
                   value = textFieldValue,
                   onValueChange = {
                       if (isPhoneNumberField) {
                           if (it.length <= 11 && it.all { char -> char.isDigit() }) {
                               onValueChange(it)
                           }
                       } else {
                           onValueChange(it)
                       }
                   },
                   placeholder = { Text("Enter value") },
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(51.dp),
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
                   colors = OutlinedTextFieldDefaults.colors(
                       unfocusedContainerColor = Color.Transparent
                   )
               )
           }
        }
    }
}

@Composable
fun FormRadioButton(selectedOption: String, onOptionSelected: (String) -> Unit) {
    val activation = listOf( "SuperAdmin", "Admin", "Residence")
    Row (
       modifier = Modifier
           .fillMaxWidth()
           .padding(top = 6.dp),
       horizontalArrangement = Arrangement.Center
    ){
       activation.forEach { text ->
           Row(
               Modifier
                  .selectable(
                      selected = (text == selectedOption),
                      onClick = {
                         onOptionSelected(text)
                      }
                  )
           ){
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
                 modifier = Modifier.padding(start = 8.dp)
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
           .padding(top = 4.dp)
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
fun DatePickerField(label: String, dateValue: String, onDateChange: (String) -> Unit) {
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
                   Text(
                       text = displayDate,
                       fontFamily = FontFamily.SansSerif,
                       modifier = Modifier
                           .padding(10.dp)
                           .align(Alignment.CenterStart)
                           , fontSize = 17.sp
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

@Composable
fun ButtonSubmitData(statesValue: Map<String, MutableState<String>>, selectedOption: String,
      isActiveState: Boolean, onSubmit: (UserDto) -> Unit) {
    Button(
        onClick = {
            val userDto = UserDto(
                firstName = statesValue["First Name"]?.value ?: "",
                middleName = statesValue["Middle Name"]?.value ?: "",
                lastName = statesValue["Last Name"]?.value ?: "",
                email = statesValue["Email"]?.value ?: "",
                mobileNumber = statesValue["Mobile Number"]?.value ?: "",
                dateOfBirth = statesValue["Date Of Birth"]?.value ?: "",
                password = statesValue["Password"]?.value ?: "",
                isSuperAdmin = selectedOption == "SuperAdmin",
                isAdmin = selectedOption == "Admin",
                isResidence = selectedOption == "Residence",
                isActive = isActiveState
            )
            onSubmit(userDto)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 7.dp)
            .height(54.dp),
           colors = ButtonDefaults.buttonColors(
               containerColor = Color(0xFF6650a4),
               contentColor = Color(0xFFFFFFFF)
           ),
        ){
        Text("Submit", fontSize = 18.sp)
    }
}