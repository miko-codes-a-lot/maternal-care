package org.maternalcare.modules.main.user.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.maternalcare.modules.main.user.model.UserDto

@Preview(showBackground = true)
@Composable
fun UserFormPreview() {
    UserForm()
}

@Composable
fun UserForm(onSubmit: suspend (UserDto) -> Unit = {}) {
    var firstName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(true) }
    var isSuperAdmin by remember { mutableStateOf(false) }
    var isAdmin by remember { mutableStateOf(false) }
    var isResidence by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = middleName,
            onValueChange = { middleName = it },
            label = { Text("Middle Name") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = mobileNumber,
            onValueChange = { mobileNumber = it },
            label = { Text("Mobile Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = dateOfBirth,
            onValueChange = { dateOfBirth = it },
            label = { Text("Date of Birth") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Checkbox(
                checked = isSuperAdmin,
                onCheckedChange = {
                    if (it) {
                        isSuperAdmin = true
                        isAdmin = false
                        isResidence = false
                    }
                }
            )
            Text(text = "Is Super Admin")

            Checkbox(
                checked = isAdmin,
                onCheckedChange = {
                    if (it) {
                        isSuperAdmin = false
                        isAdmin = true
                        isResidence = false
                    }
                }
            )
            Text(text = "Is Admin")
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Checkbox(
                checked = isResidence,
                onCheckedChange = {
                    if (it) {
                        isSuperAdmin = false
                        isAdmin = false
                        isResidence = true
                    }
                }
            )
            Text(text = "Is Residence")

            Checkbox(
                checked = isActive,
                onCheckedChange = { isActive = it }
            )
            Text(text = "Is Active")
        }

        Button(
            onClick = {
                val userDto = UserDto(
                    firstName = firstName,
                    middleName = middleName,
                    email = email,
                    mobileNumber = mobileNumber,
                    dateOfBirth = dateOfBirth,
                    password = password,
                    isActive = isActive,
                    isSuperAdmin = isSuperAdmin,
                    isAdmin = isAdmin,
                    isResidence = isResidence
                )

                coroutineScope.launch {
                    onSubmit(userDto)
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Create User")
        }
    }
}
