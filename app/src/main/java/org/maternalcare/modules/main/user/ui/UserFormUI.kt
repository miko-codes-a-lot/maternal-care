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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.maternalcare.R
import org.maternalcare.modules.main.MainNav
import org.maternalcare.modules.main.user.model.UserDto
import java.util.Calendar

@Preview(showBackground = true)
@Composable
fun UserFormPreview() {
    UserForm(navController = rememberNavController())
}

@Composable
fun UserForm(title : String = "Create Account", onSubmit: suspend (UserDto) -> Unit = {},navController: NavController) {
    val coroutineScope = rememberCoroutineScope()

    val listOfLabel = listOf(
        "First Name", "Middle Name", "Last Name", "Email", "Mobile Number",
        "Date Of Birth", "Password"
    )

    val statesValue = remember { listOfLabel.associateWith { mutableStateOf("")} }

    var selectedOption by remember { mutableStateOf("") }

    var isActive by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title,
            fontFamily = FontFamily.Serif,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(bottom = 10.dp, top = 12.dp)
        )

        ContainerLabelValue(statesValue)

        Spacer(modifier = Modifier.padding(top = 30.dp))

        FormRadioButton(selectedOption = selectedOption, onOptionSelected =  { selectedOption = it } )

        SwitchButton(isActiveState = remember { mutableStateOf(isActive) },
            scale = 0.7f, switchText = "Active")

        ButtonSubmitData(statesValue, selectedOption, coroutineScope, onSubmit)

        Spacer(modifier = Modifier.padding(top = 13.dp))

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
                .padding(top = 10.dp)
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
           .padding(bottom = 14.dp),
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
                 fontSize = 17.sp,
                 fontWeight = FontWeight.Bold,
                 modifier = Modifier.padding(start = 8.dp)
             )
             Spacer(modifier = Modifier.width(19.dp))
           }
       }
   }
}

@Composable
fun SwitchButton(isActiveState: MutableState<Boolean>, scale: Float, switchText : String) {
   Box(
       modifier = Modifier
           .fillMaxWidth()
           .padding(0.dp)
   ){
     Switch(
         checked = isActiveState.value,
         onCheckedChange = {
             isActiveState.value = it
         },
         modifier = Modifier
             .scale(scale)
             .padding(0.dp),
         thumbContent = if (isActiveState.value) {
             {
                 Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
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
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                onDateChange(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
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
               ){
                   Text(
                       text = dateValue.ifEmpty { "Select Date" },
                       fontFamily = FontFamily.SansSerif,
                       modifier = Modifier
                           .padding(10.dp)
                           .align(Alignment.CenterStart)
                           , fontSize = 17.sp
                   )
                   Divider(
                       color = Color.Gray,
                       thickness = 1.dp,
                       modifier = Modifier
                           .align(Alignment.BottomStart)
                           .fillMaxWidth()
                   )
               }
           }
       }
   }
}

@Composable
fun ButtonSubmitData(statesValue: Map<String, MutableState<String>>, selectedOption: String,
     coroutineScope: CoroutineScope, onSubmit: suspend (UserDto) -> Unit) {
    val isActiveState = remember { mutableStateOf(false) }

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
                isActive = isActiveState.value
            )
            coroutineScope.launch {
                onSubmit(userDto)
            }
        },
        modifier = Modifier
           .fillMaxWidth()
           .padding(top = 20.dp)
           .height(54.dp)
        ){
        Text("Submit", fontSize = 18.sp)
    }
}